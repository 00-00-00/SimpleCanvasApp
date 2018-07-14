package com.ground0.squareup.model

import android.graphics.RectF

class Rectangle {

    var startX: Float? = null
    var startY: Float? = null
    var endX: Float? = null
    var endY: Float? = null

    fun toRectF(): RectF {
        val startX = startX
        val startY = startY
        val endX = endX
        val endY = endY
        if (startX == null || startY == null || endX == null || endY == null) throw IllegalStateException("The vertices of the rectangle are not set")
        return RectF(startX, startY, endX, endY)
    }

    fun getAllSides(): List<Pair<Float, Float>> {
        val startX = startX
        val startY = startY
        val endX = endX
        val endY = endY
        if (startX == null || startY == null || endX == null || endY == null) throw IllegalStateException("The vertices of the rectangle are not set")
        return listOf(Pair(startX, endY), Pair(endX, endY), Pair(endX, startY), Pair(startX, startY))
    }

    override
    fun toString(): String = "coordinates: (x1: $startX, y1: $startY), x2: $endX, y2: $endY"

    class Builder {
        private val rectangle: Rectangle = Rectangle()
        fun startVertices(x: Float, y: Float): Builder {
            rectangle.apply { startX = x; startY = y }
            return this@Builder
        }

        fun endVertices(x: Float, y: Float): Builder {
            rectangle.apply { endX = x; endY = y }
            return this@Builder
        }

        fun sides(length: Float, breadth: Float): Builder {
            val startX = rectangle.startX
            val startY = rectangle.startY

            if (startX == null || startY == null) throw IllegalStateException("The start vertices are not set")

            rectangle.apply {
                endX = startX + length
                endY = startY + breadth
            }
            return this@Builder
        }

        fun build(): Rectangle {
            //Add validation and throw exception
            return rectangle
        }
    }
}