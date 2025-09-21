package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import com.kursx.parser.fb2.FictionBook

abstract class BaseViewerViewModel {

    abstract val error: MutableState<String>
    abstract val bookSample: MutableState<FictionBook?>

    abstract fun initScreenData(context: Context)
}
