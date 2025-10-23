package me.alexandervortex.shelfie.features.mediaviewer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.mediaplayer.TtsController
import me.alexandervortex.shelfie.ui.model.BookUI

private const val CHANNEL_ID = "mock_player"
private const val ACTION_PLAY = "action_play"
private const val ACTION_PAUSE = "action_pause"

@AndroidEntryPoint
class MediaService : Service() {

    // ---- публичный реактивный стейт
    private val _state = MutableStateFlow(MediaServiceState())
    val state = _state.asStateFlow()

    // binder для связи с VM
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {

        fun getService(): MediaService = this@MediaService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    // infra
    private lateinit var mediaSession: MediaSessionCompat
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // TTS
    private var ttsController: TtsController? = null
    private var playingBook: BookUI? = null

    override fun onCreate() {
        super.onCreate()
        ensureNotificationChannel()
        mediaSession = MediaSessionCompat(this, "ShelfieTTS").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() = updatePlayback(true, _state.value.index)
                override fun onPause() = updatePlayback(false, _state.value.index)
            })
            isActive = true
        }

        startForeground(1, buildNotification(isPlaying = false))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> updatePlayback(true, _state.value.index)
            ACTION_PAUSE -> updatePlayback(false, _state.value.index)
        }
        return START_STICKY
    }

    // ---- публичные команды (VM может дергать через binder)
    fun loadBook(book: BookUI?) = initTtsController(book)

    fun togglePlayPause(indexToStart: Int) {
        if (ttsController == null && playingBook != null) {
            initTtsController(playingBook)
        }
        if (ttsController == null) {
            _state.update { it.copy(error = "Книга не загружена — нечего воспроизводить") }
            return
        }
        updatePlayback(!_state.value.isPlaying, indexToStart)//
    }

    // ---- приватная логика
    private fun initTtsController(bookUI: BookUI?) {
        if (bookUI == null) {
            _state.update { it.copy(error = "Пустая книга") }
            return
        }
        playingBook = bookUI
        ttsController?.release()

        _state.update {
            it.copy(
                index = bookUI.progressIndex,
                part = 0,
                author = bookUI.titleInfo.author,
                title = bookUI.titleInfo.title
            )
        }

        ttsController = TtsController(
            context = this,
            bookModel = bookUI,
            errorAction = { msg -> _state.update { it.copy(error = msg) } },
            scrollToIndex = { idx, part ->
                _state.update { it.copy(index = idx ?: 0, part = part ?: 0) }
            },
            onStateChanged = { isPlaying ->
                _state.update {
                    it.copy(isPlaying = isPlaying)
                }
                // синхронизируем плитку в шторке
                startForeground(1, buildNotification(_state.value.isPlaying))
            }
        )
    }

    private fun updatePlayback(isPlaying: Boolean, indexToStartPlaying: Int) {
        // если контроллера нет, выходим (доп. защита)
        val ctrl = ttsController ?: run {
            _state.update { it.copy(error = "Книга не загружена — нечего воспроизводить") }
            return
        }

        _state.update {
            it.copy(isPlaying = isPlaying)
        }
        startForeground(1, buildNotification(isPlaying))
        ctrl.togglePlayPause(indexToStartPlaying)
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        val label = if (isPlaying) "паусе" else "плау"
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        val intent = PendingIntent.getService(
            this, 0, Intent(this, MediaService::class.java).setAction(action),
            PendingIntent.FLAG_IMMUTABLE
        )

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(state.value.title)
            .setContentText(state.value.author)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_service
                )
            )
            .addAction(NotificationCompat.Action(icon, label, intent))
            .setStyle(style)
            .setSmallIcon(R.drawable.ic_service)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .build()
    }

    private fun ensureNotificationChannel() {
        val nm = getSystemService(NotificationManager::class.java)
        if (nm.getNotificationChannel(CHANNEL_ID) == null) {
            val ch = NotificationChannel(
                CHANNEL_ID,
                "Shelfie Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Playback controls" } // вернул твою OLD строку
            nm.createNotificationChannel(ch)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsController?.release()
        mediaSession.release()
        scope.cancel()
    }
}