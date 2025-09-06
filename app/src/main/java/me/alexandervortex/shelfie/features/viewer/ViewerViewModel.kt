package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor() : ViewModel() {

    val error = mutableStateOf("no error")
    val bookSample: MutableState<FictionBook?> = mutableStateOf(null)

    fun initScreenData(context: Context) {
        try {
            val inputStream = context.assets.open("sample.fb2")
            val bookFile = File(context.cacheDir, "sample.fb2")
            inputStream.use { input ->
                bookFile.outputStream().use {
                    input.copyTo(it)
                }
            }
            bookSample.value = FictionBook(bookFile)
        } catch (e: Exception) {
            error.value = e.localizedMessage ?: "unknown error"
        }
    }
}