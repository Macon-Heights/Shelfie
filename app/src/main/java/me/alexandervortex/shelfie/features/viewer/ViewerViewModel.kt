package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.base.ext.toBook
import me.alexandervortex.shelfie.data.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: RepositoryImpl,
) : BaseViewerViewModel() {

    override val error = mutableStateOf("no error")
    override val bookSample: MutableState<FictionBook?> = mutableStateOf(null)

    override fun initScreenData(context: Context) {
        try {
            bookSample.value = repo.currentBook?.uri?.toUri().toBook(context)
            error.value = "no error"
        } catch (e: Exception) {
            error.value = e.localizedMessage ?: "unknown error"
        }
    }
}