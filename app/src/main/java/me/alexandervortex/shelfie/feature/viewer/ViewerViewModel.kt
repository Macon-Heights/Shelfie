package me.alexandervortex.shelfie.feature.viewer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.feature.player.MediaService
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerEffect
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerIntent
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerState
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
    private val factory: ViewerUIFactory
) : ViewModel() {

    private val _state = MutableStateFlow(ViewerState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ViewerEffect>()
    val effect = _effect.asSharedFlow()

    private var service: MediaService? = null // todo leaked context
    private var serviceJob: Job? = null
    private var isBound = false

    fun onIntent(intent: ViewerIntent) {
        when (intent) {
            is ViewerIntent.LoadBook -> loadCurrentBook(intent.id)
            is ViewerIntent.BindService -> bindService(intent.context)
            is ViewerIntent.UnbindService -> unbindService(intent.context)
            is ViewerIntent.TogglePlayPause -> togglePlayPause(intent.index)
            is ViewerIntent.SaveScrollStateOnDispose -> saveScrollStateOnDispose(
                intent.id,
                intent.index,
                intent.offset
            )

            ViewerIntent.Next -> service?.clickNext()
            ViewerIntent.Prev -> service?.clickPrev()
            ViewerIntent.ToggleTimer -> service?.clickTimer()
            ViewerIntent.ToggleSpeed -> service?.clickSpeed()
            ViewerIntent.ToggleMenu -> _state.update { it.copy(isMenuVisible = !it.isMenuVisible) }
            ViewerIntent.ToggleSettings -> _state.update { it.copy(isSettingsVisible = !it.isSettingsVisible) }
        }
    }

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

            newService.setOnSaveProgressListener { bookId, index, offset ->
                viewModelScope.launch {
                    repo.updateProgress(
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

    private fun bindService(context: Context) {
        if (isBound) return
        val intent = Intent(context, MediaService::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    private fun unbindService(context: Context) {
        if (!isBound) return
        runCatching {
            context.unbindService(conn)
        }
        serviceJob?.cancel()
        serviceJob = null
        service = null
        isBound = false
    }

    private fun loadCurrentBook(id: String) {
        _state.update {
            it.copy(
                book = BookUIModel(
                    id = "",
                    localPath = "",
                    titleInfo = getTitleInfo(),
                    elements = getSkeletons()
                )
            )
        }
        viewModelScope.launch {
            try {
                _state.update {
                    val model = repo.getBookModelById(id)
                    it.copy(book = factory.getBookUIModel(model))
                }
                service?.loadBook(_state.value.book)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.localizedMessage ?: "unknown viewmodel error")
                }
            }
        }
    }

    private fun getTitleInfo(): PreviewBookModel {
        return PreviewBookModel("", "", "", "", "", "", null, emptyList())
    }

    private fun getSkeletons(): List<ElementUIModel> {
        return (1..9).map { index ->
            ElementUIModel.Skeleton
        }
    }

    private fun saveScrollStateOnDispose(
        id: String,
        index: Int,
        offset: Int,
    ) {
        viewModelScope.launch {
            repo.updateProgress(
                id,
                index,
                offset,
                _state.value.book?.elements?.size ?: 0
            )
        }
    }

    private fun togglePlayPause(currentTopIndex: Int) {
        service?.togglePlayPause(currentTopIndex)
    }
}
