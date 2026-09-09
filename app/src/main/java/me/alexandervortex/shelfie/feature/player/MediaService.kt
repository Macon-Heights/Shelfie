package me.alexandervortex.shelfie.feature.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.base.MainActivity
import me.alexandervortex.shelfie.feature.settings.values.SpeechRateValue
import me.alexandervortex.shelfie.feature.settings.values.TimerValue
import me.alexandervortex.shelfie.feature.settings.values.next
import me.alexandervortex.shelfie.model.ByteImageModel
import me.alexandervortex.shelfie.ui.model.BookUIModel

private const val CHANNEL_ID = "shelfie_player"

private const val ACTION_PLAY = "action_play"
private const val ACTION_PAUSE = "action_pause"
private const val ACTION_SPEED = "action_speed"
private const val ACTION_NEXT = "action_next"
private const val ACTION_PREV = "action_prev"

@AndroidEntryPoint
class MediaService : Service() {

    private val _state = MutableStateFlow(MediaServiceState())
    val state = _state.asStateFlow()

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MediaService = this@MediaService
    }

    private var onSaveProgress: ((bookId: String, index: Int, offset: Int) -> Unit)? = null

    fun setOnSaveProgressListener(listener: (bookId: String, index: Int, offset: Int) -> Unit) {
        onSaveProgress = listener
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private lateinit var mediaSession: MediaSessionCompat

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var ttsController: TtsController? = null
    private var playingBook: BookUIModel? = null

    override fun onCreate() {
        super.onCreate()

        ensureNotificationChannel()

        mediaSession = MediaSessionCompat(this, "ShelfieTTS").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() = updatePlayback(
                    true, _state.value.index
                )

                override fun onPause() = updatePlayback(
                    false, _state.value.index
                )

                override fun onSkipToNext() {
                    clickNext()
                }

                override fun onSkipToPrevious() {
                    clickPrev()
                }

                override fun onCustomAction(
                    action: String?,
                    extras: Bundle?,
                ) {
                    when (action) {
                        ACTION_SPEED -> clickSpeed()
                    }
                }
            })

            setSessionActivity(createContentPendingIntent())
            isActive = true
        }

        updateMediaSession()
        updateForeground(false)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> updatePlayback(true, _state.value.index)
            ACTION_PAUSE -> updatePlayback(false, _state.value.index)
            ACTION_SPEED -> clickSpeed()
            ACTION_NEXT -> clickNext()
            ACTION_PREV -> clickPrev()
            else -> updateForeground()
        }
        return START_STICKY
    }

    fun togglePlayPause(indexToStart: Int) {
        if (ttsController == null) {
            playingBook?.let {
                initTtsController(it)
            }
            _state.update { it.copy(error = "Nothing to play") }
            return
        }
        updatePlayback(!_state.value.isPlaying, indexToStart)//
    }

    fun loadBook(book: BookUIModel?) {
        if (book == null) {
            _state.update { it.copy(error = "Empty Book") }
            return
        }

        if (playingBook == null) {
            initTtsController(book)
            return
        }

        scope.launch {
            ttsController?.release()

            _state.update {
                it.copy(
                    isPlaying = false,
                    index = book.progressIndex,
                    offset = book.progressOffset,
                    title = book.titleInfo.title,
                    author = book.titleInfo.author,
                    error = ""
                )
            }
            playingBook = book

            updateMediaSession()

            initTtsController(book)
        }
    }

    private fun initTtsController(bookUIModel: BookUIModel) {
        playingBook = bookUIModel
        ttsController?.release()

        _state.update {
            it.copy(
                index = bookUIModel.progressIndex,
                offset = bookUIModel.progressOffset,
                author = bookUIModel.titleInfo.author,
                title = bookUIModel.titleInfo.title,
                speed = SpeechRateValue.DEFAULT,
                isPlaying = false,
                timer = TimerValue.OFF
            )
        }
        updateMediaSession()

        ttsController =
            TtsController(
                context = this, bookModel = bookUIModel, errorAction = { msg ->
                _state.update { it.copy(error = msg) }
            },
            scrollToIndex = { idx, part ->
                _state.update { it.copy(index = idx ?: 0, offset = part ?: 0) }
                updatePlaybackState()
            },
            onStateChanged = { isPlaying ->
                _state.update {
                    it.copy(isPlaying = isPlaying)
                }

                updatePlaybackState()
                updateForeground()
            }, saveScrollState = { id, index, offset ->
                onSaveProgress?.invoke(id, index, offset)
            }
        )
    }

    private fun updatePlayback(isPlaying: Boolean, indexToStartPlaying: Int) {
        val ctrl = ttsController ?: run {
            _state.update { it.copy(error = "Nothing to play") }
            return
        }

        _state.update {
            it.copy(isPlaying = isPlaying)
        }

        updatePlaybackState()
        updateForeground()
        ctrl.togglePlayPause(indexToStartPlaying)
    }

    private fun updateMediaSession() {
        updateMediaMetadata()
        updatePlaybackState()
    }

    private fun updateMediaMetadata() {
        val book = playingBook ?: return

        val builder = MediaMetadataCompat.Builder().putString(
            MediaMetadataCompat.METADATA_KEY_TITLE,
            book.titleInfo.title,
        ).putString(
            MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
            book.titleInfo.title,
        ).putString(
            MediaMetadataCompat.METADATA_KEY_ARTIST,
            book.titleInfo.author,
        )

        getBookCover(book)?.let { bitmap ->
            builder.putBitmap(
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                bitmap,
            )

            builder.putBitmap(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON,
                bitmap,
            )
        }

        mediaSession.setMetadata(
            builder.build()
        )
    }

    private fun updatePlaybackState() {
        if (!::mediaSession.isInitialized) return

        val playbackState = if (_state.value.isPlaying) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }

        val actions =
            PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder().setActions(actions).setState(
                playbackState,
                PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                _state.value.speed.speed,
            ).addCustomAction(
                PlaybackStateCompat.CustomAction.Builder(
                    ACTION_SPEED,
                    "Playback speed",
                    R.drawable.ic_speed,
                ).build()
            ).build()
        )
    }

    private fun updateForeground(isPlaying: Boolean = _state.value.isPlaying) {
        val notification = buildNotification(isPlaying)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(1, notification)
        }
    }

    private fun buildNotification(isPlaying: Boolean): Notification {

        val playPauseAction = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        val playPauseLabel = if (isPlaying) "Pause" else "Play"

        val prevIntent = PendingIntent.getService(
            this, 1, Intent(this, MediaService::class.java).setAction(ACTION_PREV),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val playPauseIntent = PendingIntent.getService(
            this,
            2,
            Intent(
                this, MediaService::class.java
            ).setAction(playPauseAction),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val nextIntent = PendingIntent.getService(
            this,
            3,
            Intent(
                this, MediaService::class.java
            ).setAction(ACTION_NEXT),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val style = androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(
            mediaSession.sessionToken
        ).setShowActionsInCompactView(
            0,
            1,
            2,
        )
        return NotificationCompat.Builder(
            this,
            CHANNEL_ID,
        ).setContentTitle(
            state.value.title
        ).setContentText(
            state.value.author
        )
            .setContentIntent(
            createContentPendingIntent()
        )
            .setLargeIcon(
            playingBook?.let(::getBookCover) ?:
            BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_service
                )
        )
            .addAction(NotificationCompat.Action(R.drawable.ic_speed, "Previous", prevIntent,))
            .addAction(NotificationCompat.Action(playPauseIcon, playPauseLabel, playPauseIntent,))
            .addAction(NotificationCompat.Action(R.drawable.ic_speed, "Next", nextIntent,))
            .setStyle(style).setSmallIcon(R.drawable.ic_service)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true).setOngoing(isPlaying).build()
    }


    private fun ensureNotificationChannel() {
        val nm = getSystemService(NotificationManager::class.java)
        if (nm.getNotificationChannel(CHANNEL_ID) == null) {
            val ch = NotificationChannel(
                CHANNEL_ID,
                "Shelfie Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Playback controls" }
            nm.createNotificationChannel(ch)
        }
    }

    private fun getBookCover(
        book: BookUIModel,
    ): Bitmap? {
        val bytes = (book.titleInfo.coverImage as? ByteImageModel)?.image ?: return null

        return BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsController?.release()
        mediaSession.release()
        scope.cancel()
    }

    fun clickTimer() {
        _state.update {
            it.copy(timer = it.timer.next())
        }
        ttsController?.updateTimer(state.value.timer)
    }

    private fun createContentPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            100,
            Intent(
                this,
                MainActivity::class.java,
            ).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    fun clickSpeed() {
        _state.update {
            it.copy(speed = it.speed.getNext())
        }
        ttsController?.updateSpeechRate(state.value.speed)
        updatePlaybackState()
    }

    fun clickNext() {
        ttsController?.changePlayPosition(+1)
        updatePlaybackState()
    }

    fun clickPrev() {
        ttsController?.changePlayPosition(-1)
        updatePlaybackState()
    }
}