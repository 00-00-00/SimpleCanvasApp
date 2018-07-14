package com.ground0.squareup.view

import android.widget.SeekBar

class SeekBarChangeListener(val seekBarChangeCallback: SeekBarChangeCallback?)
    : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progess: Int, fromUser: Boolean) {
        if (fromUser) {
            seekBarChangeCallback?.onProgress(progess)
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.let { seekBarChangeCallback?.onFinish(it.progress) }
    }
}