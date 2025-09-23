package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.model.FB2Model
import me.alexandervortex.shelfie.data.model.toFB2Model
import me.alexandervortex.shelfie.data.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: RepositoryImpl,
) : BaseViewerViewModel() {

    override val error = mutableStateOf("no error")
    override val bookSample: MutableState<FB2Model?> = mutableStateOf(null)

    override fun initScreenData(context: Context) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getTheBook().toFB2Model()
                error.value = "no error"
            } catch (e: Exception) {
                error.value = e.localizedMessage ?: "unknown error"
            }
        }
    }
}