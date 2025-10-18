package me.alexandervortex.shelfie.features.mediaplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.features._deprecated_viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

@HiltViewModel
class TtsViewModel @Inject constructor(
    app: Application,
) : AndroidViewModel(app) {

    private var service: PlayerService? = null
    private var serviceJob: Job? = null
    private var bookUI: BookUI? = null

    private val _state = MutableStateFlow(ServiceState())
    val state: StateFlow<ServiceState> = _state.asStateFlow()

    private var isBound = false

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d("${TAG}_TtsVm", "onServiceConnected:${name?.className}:${binder?.isBinderAlive}")
            val newService = (binder as? PlayerService.LocalBinder)?.getService() ?: return
            service = newService
            isBound = true

            serviceJob?.cancel()
            serviceJob = viewModelScope.launch {
                newService.state.collect {
                    Log.d("${TAG}_TtsVm", "state_collect:${_state.value}:${it}")
                    _state.value = it
                }
            }

            // важное: пробуем догрузить последнюю книгу, если она уже известна
            bookUI?.let {
                Log.d("${TAG}_TtsVm", "loadBook:${it}")
                newService.loadBook(it)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("${TAG}_TtsVm", "onServiceDisconnected:${name}")
            serviceJob?.cancel()
            serviceJob = null
            service = null
            isBound = false
        }
    }

    fun bindService(context: Context) {
        if (isBound) return
        val intent = Intent(context, PlayerService::class.java)
        // гарантируем живой сервис
        context.startForegroundService(intent)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    /** вызывать при dispose или onStop */
    fun unbindService(context: Context) {
        if (!isBound) return
        Log.d("${TAG}_TtsVm", "onCleared")
        runCatching {
            Log.d(
                "${TAG}_TtsVm",
                "runCatching"
            )
            context.unbindService(conn)
        }
            .onFailure { Log.e("${TAG}_TtsVm", "onCleared failed", it) }
        serviceJob?.cancel()
        serviceJob = null
        service = null
        isBound = false
    }

    fun loadBook(book: BookUI) {
        Log.d("${TAG}_TtsVm", "loadBook:${book.elements.size}")
        bookUI = book // with progress index
//        _state.value = state.value.copy(index = book.progressIndex)
        service?.loadBook(book)
    }

    fun togglePlayPause(currentTopIndex: Int) {
        Log.d("${TAG}_TtsVm", "togglePlayPause:${currentTopIndex}")
        service?.togglePlayPause(currentTopIndex)
    }
}
