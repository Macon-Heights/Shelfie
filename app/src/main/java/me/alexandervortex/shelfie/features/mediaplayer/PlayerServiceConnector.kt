package me.alexandervortex.shelfie.features.mediaplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Синглтон-класс, который живёт на уровне Application и умеет:
 *  - подключаться к PlayerService,
 *  - читать текущее состояние (ServiceState),
 *  - использоваться из любой ViewModel.
 */
@Singleton
class PlayerServiceConnector @Inject constructor(
    private val app: Application,
) {

    private var service: PlayerService? = null
    private var isBound = false

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(ServiceState())
    val state: StateFlow<ServiceState> = _state.asStateFlow()

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d("${TAG}_Connector", "onServiceConnected:$name")
            val srv = (binder as? PlayerService.LocalBinder)?.getService() ?: return
            service = srv
            scope.launch {
                srv.state.collect { _state.value = it }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("${TAG}_Connector", "onServiceDisconnected")
            service = null
        }
    }

    /** вызываем при старте приложения (или лениво при первом запросе) */
    fun ensureConnected() {
        if (isBound) return
        val ctx = app
        val intent = Intent(ctx, PlayerService::class.java)
        ctx.startForegroundService(intent)
        ctx.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    fun togglePlayPause(index: Int) = service?.togglePlayPause(index)
    fun loadBook(book: BookUI) = service?.loadBook(book)
    fun getCurrentBook(): BookUI? = service?.getCurrentBook()
}
