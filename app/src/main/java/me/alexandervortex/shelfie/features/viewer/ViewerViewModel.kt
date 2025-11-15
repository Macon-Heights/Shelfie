package me.alexandervortex.shelfie.features.viewer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.player.MediaService
import me.alexandervortex.shelfie.features.player.MediaServiceState
import me.alexandervortex.shelfie.ui.model.BookUIModel
import javax.inject.Inject

/**
 * // screenBook выставлен, книга загружена и готова к чтению
 * // сразу же отображаю ее на экране, чтобы в любом из кейсов пользователь начал ее листать
 * // нажали книгу в каталоге
 * // она загружается из памяти и вгружается в сервис, чтобы воспроизводиться
 * // когда она воспроизводится, она уже в сервисе, а так же в репозитории
 * // когда я выгружаю приложение из памяти, у меня создается новая вьюмодель
 * // новая вьюмодель цепляется к существующему сервису и проверяет, ждал ли он ее
 * // если он ее не ждал и был занят воспроизведением - если это та же книга - окей, продолжаем
 * // если это другая книга, то продолжаем воспроизводить старую (отображая ее название внизу)
 * // как теперь мне воспроизвести текущую книгу, если плеер уже занят другой?
 * //
 * //
 * // сделать кнопку точкой входа в плеер?
 * // нажимаю воспроизвести - открывается плажка с кнопками скорости, пауза, переключение глав, прогрессом
 * // нажимаю паузу и все сворачивается в одну кнопочку "плей" занаво?
 *
 * // каррентбук должен сохраняться в плеер при нажатии ПЛЕЙ
 * // если сделать заранее - то при прослушивании книги А -
 * // ты зайдешь на книгу Б и перезатрешь "currentBook"
 * // а значит придется проверять книга на экране и книга в сервисе - это одна книга? (В сервисе)
 */
@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
) : ViewModel() {

    val errorState = mutableStateOf("")

    private var service: MediaService? = null
    private var serviceJob: Job? = null
    val bookUIModel: MutableState<BookUIModel?> = mutableStateOf(null)

    private val _state = MutableStateFlow(MediaServiceState())
    val state: StateFlow<MediaServiceState> = _state.asStateFlow()

    private var isBound = false

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val newService = (binder as? MediaService.LocalBinder)?.getService() ?: return
            service = newService
            isBound = true

            serviceJob?.cancel() // check later // MB WE NEED IS_ACTIVE
            serviceJob = viewModelScope.launch {
                newService.state.collect {
                    _state.value = it
                }
            }

            // 💾 Передаём callback для сохранения прогресса
            newService.setOnSaveProgressListener { bookId, index, offset ->
                viewModelScope.launch {
                    repo.saveCurrentBookProgress(
                        bookId,
                        index,
                        offset,
                        bookUIModel.value?.elements?.size ?: 0
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
                bookUIModel.value = repo.getBookModelById(id)
                service?.loadBook(bookUIModel.value)

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
                bookUIModel.value?.elements?.size ?: 0
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
