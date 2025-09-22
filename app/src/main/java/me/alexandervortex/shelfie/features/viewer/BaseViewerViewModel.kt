package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.data.model.FB2Model

abstract class BaseViewerViewModel : ViewModel() {

    abstract val error: MutableState<String>
    abstract val bookSample: MutableState<FB2Model?>

    abstract fun initScreenData(context: Context)
}
