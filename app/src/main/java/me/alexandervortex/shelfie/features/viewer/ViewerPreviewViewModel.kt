package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kursx.parser.fb2.FictionBook

class ViewerPreviewViewModel : BaseViewerViewModel() {

    override val error: MutableState<String> = mutableStateOf("no error")

    override val bookSample: MutableState<FictionBook?> = mutableStateOf(null)

    override fun initScreenData(context: Context) {

    }
}
