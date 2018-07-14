package com.ground0.squareup

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {

    @BindView(R.id.a_main_canvas)
    lateinit var imageView: ImageView

    private val rectangleBuilder = Rectangle.Builder()
    private val rectangles: ArrayList<Rectangle> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

    override fun onResume() {
        super.onResume()
        var canvas: Canvas? = null
        initTouchInterception(imageView, object : TouchCallback {
            override fun onStart(x: Float, y: Float) {
                canvas = canvas ?: initCanvas(imageView)
                rectangleBuilder.startRectangle(x, y)
            }

            override fun onStop(x: Float, y: Float) {
                rectangleBuilder.finishRectangle(x, y)
                //save rectangle
                rectangleBuilder.build().also { rectangle ->
                    rectangles.add(rectangle)
                    canvas?.let { drawRectangle(imageView, it, rectangle) }
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initTouchInterception(targetImage: ImageView, callback: TouchCallback?) {
        targetImage.setOnTouchListener { view, motionEvent ->
            if (view !is ImageView) return@setOnTouchListener false
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(this.localClassName, "$view is being touched")
                    Log.d(this.localClassName, "Touched at ${motionEvent.x} and ${motionEvent.y}")
                    callback?.onStart(motionEvent.x, motionEvent.y)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(this.localClassName, "$view was stopped being touced")
                    Log.d(this.localClassName, "Touch let go at ${motionEvent.x} and ${motionEvent.y}")

                    callback?.onStop(motionEvent.x, motionEvent.y)
                    true
                }

                else -> false

            }
        }
    }

    private fun initCanvas(imageView: ImageView): Canvas {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(bitmap)
        imageView.setImageBitmap(bitmap)
        return canvas
    }

    private fun drawRectangle(imageView: ImageView, canvas: Canvas, rectangle: Rectangle) {

        canvas.drawRect(rectangle.getRect(), Paint()
                .apply {
                    style = Paint.Style.STROKE
                    color = Color.BLUE
                    strokeWidth = 5F //NO MAGIC NUMBERS!!
                })

        imageView.invalidate()
    }

}

interface TouchCallback {
    fun onStart(x: Float, y: Float)
    fun onStop(x: Float, y: Float)
}

class Rectangle {
    private var startX: Float? = null
    private var startY: Float? = null
    private var endX: Float? = null
    private var endY: Float? = null

    fun getRect(): RectF {
        val startX = startX
        val startY = startY
        val endX = endX
        val endY = endY
        if (startX == null || startY == null || endX == null || endY == null) throw IllegalStateException("The vertices of the rectangle are not set")
        return RectF(startX, startY, endX, endY)
    }

    class Builder {
        private val rectangle: Rectangle = Rectangle()
        fun startRectangle(x: Float, y: Float): Builder {
            rectangle.apply { startX = x; startY = y }
            return this@Builder
        }

        fun finishRectangle(x: Float, y: Float): Builder {
            rectangle.apply { endX = x; endY = y }
            return this@Builder
        }

        fun build(): Rectangle {
            //Add validation and throw exception
            return rectangle
        }
    }
}