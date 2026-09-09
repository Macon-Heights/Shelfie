package me.alexandervortex.shelfie.feature.viewer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
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
import me.alexandervortex.shelfie.ui.model.UI
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
    private val factory: ViewerUIFactory,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(ViewerState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ViewerEffect>()
    val effect = _effect.asSharedFlow()

    private var serviceRef: WeakReference<MediaService>? = null
    private val service: MediaService? get() = serviceRef?.get()
    private var serviceJob: Job? = null
    private var isBound = false

    fun onIntent(intent: ViewerIntent) {
        when (intent) {
            is ViewerIntent.LoadBook -> loadCurrentBook(intent.id)
            ViewerIntent.BindService -> bindService()
            ViewerIntent.UnbindService -> unbindService()
            is ViewerIntent.TogglePlayPause -> togglePlayPause(intent.index)
            is ViewerIntent.SaveScrollStateOnDispose -> saveScrollStateOnDispose(
                intent.id,
                intent.index,
                intent.offset
            )

            ViewerIntent.Next -> service?.clickNext()
            ViewerIntent.Sections -> _state.update { it.copy(isSectionsVisible = !it.isSectionsVisible) }
            ViewerIntent.ToggleTimer -> service?.clickTimer()
            ViewerIntent.ToggleSpeed -> service?.clickSpeed()
            ViewerIntent.ToggleMenu -> _state.update { it.copy(isMenuVisible = !it.isMenuVisible) }
            ViewerIntent.ToggleSettings -> _state.update { it.copy(isSettingsVisible = !it.isSettingsVisible) }
            ViewerIntent.ToggleSections -> _state.update { it.copy(isSectionsVisible = !it.isSectionsVisible) }
        }
    }

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val newService = (binder as? MediaService.LocalBinder)?.getService() ?: return
            serviceRef = WeakReference(newService)
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
            serviceRef = null
            isBound = false
        }
    }

    private fun bindService() {
        if (isBound) return
        val intent = Intent(context, MediaService::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    private fun unbindService() {
        if (!isBound) return
        service?.setOnSaveProgressListener { _, _, _ -> }
        runCatching {
            context.unbindService(conn)
        }
        serviceJob?.cancel()
        serviceJob = null
        serviceRef = null
        isBound = false
    }

    override fun onCleared() {
        super.onCleared()
        unbindService()
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
                val model = repo.getBookModelById(id)
                val uiModel = factory.getBookUIModel(model)
                _state.update {
                    it.copy(book = uiModel)
                }
                service?.loadBook(_state.value.book)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                _state.update {
                    it.copy(error = e.localizedMessage ?: "unknown viewmodel error")
                }
            }
        }
    }

    private fun getTitleInfo(): PreviewBookModel {
        return PreviewBookModel("", "", "", "", "", "", null, emptyList())
    }

    private fun getSkeletons(): List<UI> {
        return (1..9).map { index ->
            UI.Skeleton
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
