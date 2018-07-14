package com.ground0.squareup.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.ImageView
import com.ground0.squareup.activity.SquareActivity
import com.ground0.squareup.model.Rectangle

object CanvasUtil {
    fun getCanvas(imageView: ImageView): Canvas {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageView.setImageBitmap(bitmap)
        return canvas
    }

    fun drawRectangle(imageView: ImageView, canvas: Canvas, rectangle: Rectangle) {
        val paint = Paint()
                .apply {
                    style = Paint.Style.STROKE
                    color = SquareActivity.PAINT_COLOUR
                    strokeWidth = SquareActivity.SIZE_STROKE_SIZE
                }
        canvas.drawRect(rectangle.toRectF(), paint)
        imageView.invalidate()
    }

    fun drawCircle(imageView: ImageView, canvas: Canvas, cX: Float, cY: Float) {
        val paint = Paint()
                .apply {
                    style = Paint.Style.FILL
                    color = SquareActivity.PAINT_COLOUR
                }
        canvas.drawCircle(cX, cY, SquareActivity.SIZE_STROKE_SIZE, paint)
        imageView.invalidate()
    }
}