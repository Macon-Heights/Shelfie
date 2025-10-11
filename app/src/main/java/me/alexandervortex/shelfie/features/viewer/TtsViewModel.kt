package me.alexandervortex.shelfie.features.mediaplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

@HiltViewModel
class TtsViewModel
@Inject constructor(
    app: Application,
) : AndroidViewModel(app) {

    private var service: MockPlayerService? = null
    private val _state = MutableStateFlow(ServiceState())
    val state: StateFlow<ServiceState> = _state.asStateFlow()

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as? MockPlayerService.LocalBinder)?.getService()
            // подписываемся на поток сервиса
            viewModelScope.launch {
                service!!.state.collect { _state.value = it }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }
    }

    init {
        // bind к сервису
        val ctx = getApplication<Application>()
        ctx.bindService(Intent(ctx, MockPlayerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        val ctx = getApplication<Application>()
        runCatching { ctx.unbindService(conn) }
    }

    fun loadBook(book: BookUI?) {
        service?.loadBook(book)
    }

    fun togglePlayPause(currentTopIndex: Int) {
        service?.togglePlayPause(currentTopIndex)
    }
}