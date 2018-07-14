package com.ground0.squareup.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.ground0.squareup.R
import com.ground0.squareup.core.BaseActivity
import com.ground0.squareup.model.Rectangle
import com.ground0.squareup.view.TouchCallback

class MainActivity : BaseActivity() {

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
                rectangleBuilder.startVertices(x, y)
            }

            override fun onStop(x: Float, y: Float) {
                rectangleBuilder.endVertices(x, y)
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

        canvas.drawRect(rectangle.toRectF(), Paint()
                .apply {
                    style = Paint.Style.STROKE
                    color = Color.BLUE
                    strokeWidth = 5F //NO MAGIC NUMBERS!!
                })

        imageView.invalidate()
    }

}