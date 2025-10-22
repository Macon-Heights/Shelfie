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
import android.util.Log
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
import me.alexandervortex.shelfie.features.mediaplayer.ShelfieTtsHolder
import me.alexandervortex.shelfie.features.mediaplayer.TtsController
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI

private const val CHANNEL_ID = "mock_player"
private const val ACTION_PLAY = "action_play"
private const val ACTION_PAUSE = "action_pause"
private const val ACTION_LOAD_BOOK = "action_load_book"

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
        Log.d(
            "${TAG}_MockPlayer",
            "onCreate"
        )
        mediaSession = MediaSessionCompat(this, "ShelfieTTS").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() = updatePlayback(true, _state.value.index)
                override fun onPause() = updatePlayback(false, _state.value.index)
            })
            isActive = true
        }

        startForeground(1, buildNotification(isPlaying = false))
    }

    override fun onDestroy() {
        Log.d(
            "${TAG}_MockPlayer",
            "onDestroy"
        )
        super.onDestroy()
        ttsController?.release()
        mediaSession.release()
        scope.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(
            "${TAG}_MockPlayer",
            "onStartCommand:${intent?.action}:${_state.value.index}"
        )
        when (intent?.action) {
            ACTION_PLAY -> updatePlayback(true, _state.value.index)
            ACTION_PAUSE -> updatePlayback(false, _state.value.index)
            ACTION_LOAD_BOOK -> initTtsController(ShelfieTtsHolder.currentBook)
        }
        return START_STICKY
    }

    // ---- публичные команды (VM может дергать через binder)
    fun loadBook(book: BookUI?) = initTtsController(book)

    fun togglePlayPause(indexToStart: Int) {
        Log.d(
            "${TAG}_MockPlayer",
            "togglePlayPause:${indexToStart}"
        )
        if (ttsController == null && playingBook != null) {
            Log.d("${TAG}_MockPlayer", "ttsController == null, but have book -> reinit")
            initTtsController(playingBook)
        }
        if (ttsController == null) {
            Log.d(
                "${TAG}_MockPlayer",
                "ttsController == null:${indexToStart}"
            )
            _state.update { it.copy(error = "Книга не загружена — нечего воспроизводить") }
            return
        }
        updatePlayback(!_state.value.isPlaying, indexToStart)//
    }

    // ---- приватная логика
    private fun initTtsController(bookUI: BookUI?) {
        Log.d(
            "${TAG}_MockPlayer",
            "initTtsController:${bookUI?.elements?.size}"
        )
        if (bookUI == null) {
            Log.d(
                "${TAG}_MockPlayer",
                "bookUI == null:${bookUI}"
            )
            _state.update { it.copy(error = "Пустая книга") }
            return
        }
        playingBook = bookUI
        ttsController?.release()

        _state.update {
            Log.d(
                "${TAG}_MockPlayer",
                "_state.update:${bookUI.progressIndex}"
            )
            it.copy(index = bookUI.progressIndex, part = 0)
        }

        ttsController = TtsController(
            context = this,
            bookModel = bookUI,
            errorAction = { msg -> _state.update { it.copy(error = msg) } },
            scrollToIndex = { idx, part ->
                Log.d(
                    "${TAG}_MockPlayer",
                    "scrollToIndex:${idx}:${part}"
                )
                _state.update { it.copy(index = idx ?: 0, part = part ?: 0) }
            },
            onStateChanged = { isPlaying ->
                Log.d("${TAG}_MockPlayer", "onStateChanged:${isPlaying}")
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
        Log.d("${TAG}_MockPlayer", "updatePlayback:${isPlaying}:{$indexToStartPlaying}")
        val ctrl = ttsController ?: run {
            _state.update { it.copy(error = "Книга не загружена — нечего воспроизводить") }
            return
        }

        _state.update {
            Log.d("${TAG}_MockPlayer", "_state.update_:${isPlaying}")
            it.copy(isPlaying = isPlaying)
        }
        startForeground(1, buildNotification(isPlaying))
        ctrl.togglePlayPause(indexToStartPlaying)
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        Log.d("${TAG}_MockPlayer", "buildNotification:${isPlaying}")
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
            .setContentTitle("Shelfie Reader")
            .setContentText("Now reading...")
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_service
                )
            )
            .addAction(NotificationCompat.Action(icon, label, intent))
            .setStyle(style)
//            .setSmallIcon(R.drawable.ic_service)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .build()
    }

    private fun ensureNotificationChannel() {
        Log.d(
            "${TAG}_MockPlayer",
            "ensureNotificationChannel"
        )
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
}