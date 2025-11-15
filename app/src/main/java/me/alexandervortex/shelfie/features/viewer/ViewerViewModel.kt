package me.alexandervortex.shelfie.features.viewer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.player.MediaService
import me.alexandervortex.shelfie.features.viewer.mvi.ViewerState
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
) : ViewModel() {

    val errorState = mutableStateOf("")

    private var service: MediaService? = null // todo leaked context
    private var serviceJob: Job? = null

    private val _state = MutableStateFlow(ViewerState())
    val state = _state.asStateFlow()

    private var isBound = false

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val newService = (binder as? MediaService.LocalBinder)?.getService() ?: return
            service = newService
            isBound = true

            serviceJob?.cancel() // todo: check later // MB WE NEED IS_ACTIVE
            serviceJob = viewModelScope.launch {
                newService.state.collect { newServiceState ->
                    _state.update { it.copy(serviceState = newServiceState) }
                }
            }

            // 💾 Передаём callback для сохранения прогресса
            newService.setOnSaveProgressListener { bookId, index, offset ->
                viewModelScope.launch {
                    repo.saveCurrentBookProgress(
                        bookId,
                        index,
                        offset,
                        _state.value.book?.elements?.size ?: 0
                    )
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceJob?.cancel()
            serviceJob = null
            service = null
            isBound = false
        }
    }

    fun bindService(context: Context) {
        if (isBound) return
        val intent = Intent(context, MediaService::class.java)
        // гарантируем живой сервис
        context.startForegroundService(intent)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    /** вызывать при dispose или onStop */
    fun unbindService(context: Context) {
        if (!isBound) return
        runCatching {
            context.unbindService(conn)
        }
        serviceJob?.cancel()
        serviceJob = null
        service = null
        isBound = false
    }

    fun loadCurrentBook(id: String) {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(book = repo.getBookModelById(id))
                }
                service?.loadBook(_state.value.book)
            } catch (e: Exception) {
                errorState.value = e.localizedMessage ?: "unknown viewmodel error"
            }
        }
    }

    fun saveScrollStateOnDispose(
        id: String,
        index: Int,
        offset: Int,
    ) {
        viewModelScope.launch {
            repo.saveCurrentBookProgress(
                id,
                index,
                offset,
                _state.value.book?.elements?.size ?: 0
            )
        }
    }

    fun togglePlayPause(currentTopIndex: Int) {
        service?.togglePlayPause(currentTopIndex)
    }

    fun clickTimer() {
        service?.clickTimer()
    }

    fun clickSpeed() {
        service?.clickSpeed()
    }

    fun clickNext() {
        service?.clickNext()
    }

    fun clickPrev() {
        service?.clickPrev()
    }
}
