// features/mediaplayer/MockPlayerService.kt
package me.alexandervortex.shelfie.features.mediaplayer

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
import androidx.annotation.DrawableRes
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
import me.alexandervortex.shelfie.ui.model.BookUI

data class ServiceState(
    val isPlaying: Boolean = false,
    val isScrollable: Boolean = true,
    val error: String = "",
    val index: Int = 0,
    val part: Int = 0,
    @DrawableRes val buttonIconRes: Int = R.drawable.ic_play,
)

@AndroidEntryPoint
class MockPlayerService : Service() {

    // ---- публичный реактивный стейт
    private val _state = MutableStateFlow(ServiceState())
    val state = _state.asStateFlow()

    // binder для связи с VM
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {

        fun getService(): MockPlayerService = this@MockPlayerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    // infra
    private lateinit var mediaSession: MediaSessionCompat
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // TTS
    private var ttsController: TtsController? = null
    private var currentBook: BookUI? = null

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

    override fun onDestroy() {
        super.onDestroy()
        ttsController?.release()
        mediaSession.release()
        scope.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
        updatePlayback(!_state.value.isPlaying, indexToStart)
    }

    // ---- приватная логика
    private fun initTtsController(bookUI: BookUI?) {
        if (bookUI == null) return
        currentBook = bookUI
        ttsController?.release()

        _state.update { it.copy(index = bookUI.progressIndex, part = 0) }

        ttsController = TtsController(
            context = this,
            bookModel = bookUI,
            onAppError = { msg -> _state.update { it.copy(error = msg) } },
            scrollToIndex = { idx, part ->
                _state.update { it.copy(index = idx ?: 0, part = part ?: 0) }
            },
            onIconChanged = { iconRes ->
                _state.update { it.copy(buttonIconRes = iconRes) }
                // синхронизируем плитку в шторке
                startForeground(1, buildNotification(_state.value.isPlaying))
            }
        )
    }

    private fun updatePlayback(isPlaying: Boolean, indexToStartPlaying: Int) {
        _state.update {
            it.copy(
                isPlaying = isPlaying,
                buttonIconRes = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )
        }
        startForeground(1, buildNotification(isPlaying))
        ttsController?.togglePlayPause(indexToStartPlaying)
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        val label = if (isPlaying) "Pause" else "Play"
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        val intent = PendingIntent.getService(
            this, 0, Intent(this, MockPlayerService::class.java).setAction(action),
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
                    R.drawable.ic_launcher_foreground
                )
            )
            .addAction(NotificationCompat.Action(icon, label, intent))
            .setStyle(style)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)          // вернул твою OLD строку
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

    companion object {

        const val CHANNEL_ID = "mock_player"
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_LOAD_BOOK = "action_load_book"
    }
}