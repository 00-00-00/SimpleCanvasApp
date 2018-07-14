package com.ground0.squareup.view

interface TouchCallback {
    fun onStart(x: Float, y: Float)
    fun onStop(x: Float, y: Float)
}

interface SeekBarChangeCallback {
    fun onFinish(progress: Int)
    fun onProgress(progress: Int)
}