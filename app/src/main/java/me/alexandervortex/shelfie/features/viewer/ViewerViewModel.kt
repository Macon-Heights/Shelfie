package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.data.repository.BookRepo
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepo,
) : ViewModel() {

    val error = mutableStateOf("no error")
    val bookSample: MutableState<FictionBook?> = mutableStateOf(null)

    /**
     * Загружает книгу из App.uri, а если его нет — sample.fb2 из assets.
     */
    fun initScreenData(context: Context) {
        try {
            val file: File = repo.currentBook?.uri?.toUri()?.let { uri ->
                // копируем выбранный content:// во временный файл в кэше
                val outFile = File(context.cacheDir, "current.fb2")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IllegalStateException("Не удалось открыть выбранный файл")
                outFile
            } ?: run {
                // фоллбек на sample.fb2 из assets
                val outFile = File(context.cacheDir, "sample.fb2")
                context.assets.open("sample.fb2").use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                outFile
            }

            bookSample.value = FictionBook(file)
            error.value = "no error"

        } catch (e: Exception) {
            error.value = e.localizedMessage ?: "unknown error"
        }
    }
}