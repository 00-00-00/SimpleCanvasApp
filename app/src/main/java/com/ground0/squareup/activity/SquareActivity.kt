package com.ground0.squareup.activity

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.SeekBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.ground0.squareup.R
import com.ground0.squareup.core.BaseActivity
import com.ground0.squareup.model.Rectangle
import com.ground0.squareup.util.CanvasUtil.drawCircle
import com.ground0.squareup.util.CanvasUtil.drawRectangle
import com.ground0.squareup.util.CanvasUtil.getCanvas
import com.ground0.squareup.view.SeekBarChangeCallback
import com.ground0.squareup.view.SeekBarChangeListener

class SquareActivity : BaseActivity() {

    @BindView(R.id.a_square_seek)
    lateinit var seekBar: SeekBar
    @BindView(R.id.a_square_image)
    lateinit var imageView: ImageView

    private var canvas: Canvas? = null
    private val rectangleBuilder = Rectangle.Builder()
    private val rectangles: ArrayList<Rectangle> = arrayListOf()

    companion object {
        const val SIZE_MULTIPLIER = 5
        const val SIZE_STROKE_SIZE = 5F
        const val PAINT_COLOUR = Color.WHITE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_square)
        ButterKnife.bind(this)
        initSeekBar(seekBar, object : SeekBarChangeCallback {
            override fun onFinish(progress: Int) {

//                seekBar.visibility = View.GONE  //Much simpler to use without seekBar appearing and disappearing
                rectangleBuilder.sides((progress * SIZE_MULTIPLIER).toFloat(),
                        (progress * SIZE_MULTIPLIER).toFloat())
                //save rectangle
                rectangleBuilder.build().also { rectangle ->
                    rectangles.add(rectangle)
                    canvas?.let { drawRectangle(imageView, it, rectangle) }
                }
                //choosing not to reset the seekBar
            }

            override fun onProgress(progress: Int) {

            }
        })

    }

    @OnClick(R.id.a_square_button)
    fun onPrintButtonClick() {
        AlertDialog.Builder(this).create().apply {
            setMessage(
                    if (rectangles.isEmpty())
                        "Nothing here! Touch the screen to draw a square"
                    else
                        rectangles.joinToString(
                                separator = ", \n",
                                prefix = "[\n",
                                postfix = "\n]",
                                transform = {
                                    "{ coordinates : " +
                                            "${it.getAllSides().map {
                                                "(${it.first}, ${it.second})" +
                                                        " }"
                                            }}"
                                }
                        ))
            setButton(AlertDialog.BUTTON_POSITIVE, "Okay") { p0, p1 ->
                //do nothing
            }
            show()
        }
    }

    @OnTouch(R.id.a_square_image)
    fun onImageTouch(view: ImageView, motionEvent: MotionEvent): Boolean {
        initCanvas()
        return when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                rectangleBuilder.startVertices(motionEvent.x, motionEvent.y)
                canvas?.let { drawCircle(imageView, it, motionEvent.x, motionEvent.y) }

                seekBar.isEnabled = true
                true
            }
            else -> false
        }
    }

    private fun initCanvas() {
        canvas = canvas ?: getCanvas(imageView)
    }

    private fun initSeekBar(seekBar: SeekBar, callback: SeekBarChangeCallback) {
        seekBar.isEnabled = false
        seekBar.setOnSeekBarChangeListener(SeekBarChangeListener(callback))
    }
}
