package me.alexandervortex.shelfie.base

import android.util.Log

private const val TAG = "=^_^="

class Lg(
    val screenName: String,
) {

    fun log(msg: String) {
        Log.d("${TAG}_${screenName}", msg)
    }
}