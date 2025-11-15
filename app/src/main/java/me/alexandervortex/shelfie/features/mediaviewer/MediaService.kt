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
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.player.TtsController
import me.alexandervortex.shelfie.features.settings.values.SpeechRateValue
import me.alexandervortex.shelfie.features.settings.values.TimerValue
import me.alexandervortex.shelfie.features.settings.values.next
import me.alexandervortex.shelfie.ui.model.BookUIModel

private const val CHANNEL_ID = "mock_player"
private const val ACTION_PLAY = "action_play"
private const val ACTION_PAUSE = "action_pause"
private const val ACTION_SPEED = "action_speed"

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

    private var onSaveProgress: ((bookId: String, index: Int, offset: Int) -> Unit)? = null

    fun setOnSaveProgressListener(listener: (bookId: String, index: Int, offset: Int) -> Unit) {
        onSaveProgress = listener
    }

    override fun onBind(intent: Intent?): IBinder = binder

    // infra
    private lateinit var mediaSession: MediaSessionCompat
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // TTS
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

            })
            isActive = true
        }

        startForeground(1, buildNotification(isPlaying = false))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> updatePlayback(true, _state.value.index)
            ACTION_PAUSE -> updatePlayback(false, _state.value.index)
            ACTION_SPEED -> clickSpeed()
        }
        return START_STICKY
    }

    fun togglePlayPause(indexToStart: Int) {
        if (ttsController == null) {
            playingBook?.let {
                initTtsController(it)
            }
            _state.update { it.copy(error = "Книга не загружена — нечего воспроизводить") }
            return
        }
        updatePlayback(!_state.value.isPlaying, indexToStart)//
    }

    fun loadBook(book: BookUIModel?) {
        if (book == null) {
            _state.update { it.copy(error = "Пустая книга") }
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
                    part = 0,
                    title = book.titleInfo.title,
                    author = book.titleInfo.author,
                    error = ""
                )
            }
            playingBook = book
            initTtsController(book)
        }
    }

    private fun initTtsController(bookUIModel: BookUIModel) {
        playingBook = bookUIModel
        ttsController?.release()

        _state.update {
            it.copy(
                index = bookUIModel.progressIndex,
                part = 0,
                author = bookUIModel.titleInfo.author,
                title = bookUIModel.titleInfo.title,
                speed = SpeechRateValue.DEFAULT,
                isPlaying = false,
                timer = TimerValue.OFF
            )
        }

        ttsController = TtsController(
            context = this,
            bookModel = bookUIModel,
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
            },
            saveScrollState = { id, index, offset ->
                onSaveProgress?.invoke(id, index, offset)
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
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_speed,
                    label,
                    PendingIntent.getService(
                        this, 0, Intent(this, MediaService::class.java).setAction(ACTION_SPEED),
                        PendingIntent.FLAG_IMMUTABLE
                    )
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

    fun clickTimer() {
        _state.update {
            it.copy(timer = it.timer.next())
        }
        ttsController?.updateTimer(state.value.timer)
    }

    fun clickSpeed() {
        _state.update {
            it.copy(speed = it.speed.getNext())
        }
        ttsController?.updateSpeechRate(state.value.speed)
    }

    fun clickNext() {
        ttsController?.changePlayPosition(+1)
    }

    fun clickPrev() {
        ttsController?.changePlayPosition(-1)
    }
}