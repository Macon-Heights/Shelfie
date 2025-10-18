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
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

@HiltViewModel
class TtsViewModel @Inject constructor(
    app: Application,
) : AndroidViewModel(app) {

    // тут IDE пишет что утечка контекста
    private var service: MockPlayerService? = null
    private var serviceJob: Job? = null
    private var lastBook: BookUI? = null

    private val _state = MutableStateFlow(ServiceState())
    val state: StateFlow<ServiceState> = _state.asStateFlow()

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(
                "${TAG}_TtsVm",
                "onServiceConnected:${name?.className}:${binder?.isBinderAlive}"
            )
            val newService = (binder as? MockPlayerService.LocalBinder)?.getService() ?: return
            service = newService

            serviceJob?.cancel()
            serviceJob = viewModelScope.launch {
                newService.state.collect {
                    Log.d(
                        "${TAG}_TtsVm",
                        "state_collect:${_state.value}:${it}"
                    )
                    _state.value = it
                }
            }

            // важное: пробуем догрузить последнюю книгу, если она уже известна
            lastBook?.let {
                Log.d(
                    "${TAG}_TtsVm",
                    "loadBook:${it}"
                )
                newService.loadBook(it)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(
                "${TAG}_TtsVm",
                "onServiceDisconnected:${name}"
            )
            serviceJob?.cancel()
            service = null
        }
    }

    init {
        Log.d(
            "${TAG}_TtsVm",
            "init"
        )
        val ctx = getApplication<Application>()
        // гарантируем создание сервиса (и канал/foreground-ноти)
        ctx.startService(Intent(ctx, MockPlayerService::class.java))
        // и биндимся
        ctx.bindService(Intent(ctx, MockPlayerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        Log.d(
            "${TAG}_TtsVm",
            "onCleared"
        )
        super.onCleared()
        runCatching {
            Log.d(
                "${TAG}_TtsVm",
                "runCatching"
            )
            getApplication<Application>().unbindService(conn)
        }
        serviceJob?.cancel()
    }

    fun loadBook(book: BookUI?) {
        Log.d(
            "${TAG}_TtsVm",
            "loadBook:${book?.elements?.size}"
        )
        lastBook = book
        service?.loadBook(book)
    }

    fun togglePlayPause(currentTopIndex: Int) {
        Log.d(
            "${TAG}_TtsVm",
            "togglePlayPause:${currentTopIndex}"
        )
        service?.togglePlayPause(currentTopIndex)
    }
}
