package me.alexandervortex.shelfie.features.mediaplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import me.alexandervortex.shelfie.R

@AndroidEntryPoint
class MockPlayerService : Service() {

    private lateinit var mediaSession: MediaSessionCompat
    private val playerState = MutableStateFlow(false) // false = paused

    override fun onCreate() {
        super.onCreate()
        ensureNotificationChannel()
        mediaSession = MediaSessionCompat(this, "MockPlayer").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() = updatePlayback(true)
                override fun onPause() = updatePlayback(false)
            })
            isActive = true
        }

        startForeground(1, buildNotification(false))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> updatePlayback(true)
            ACTION_PAUSE -> updatePlayback(false)
        }
        return START_STICKY
    }

    private fun updatePlayback(isPlaying: Boolean) {
        playerState.value = isPlaying
        val notification = buildNotification(isPlaying)
        startForeground(1, notification)
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        val action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        val label = if (isPlaying) "Pause" else "Play"

        val intent = PendingIntent.getService(
            this, 0, Intent(this, MockPlayerService::class.java).setAction(action),
            PendingIntent.FLAG_IMMUTABLE
        )

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mock Reader")
            .setContentText("Now reading: 'Hardcoded sample text…'")
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            .addAction(NotificationCompat.Action(icon, label, intent))
            .setStyle(style)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {

        const val CHANNEL_ID = "mock_player"
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Shelfie Mock Player",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Playback controls"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }
}
