package com.example.trianglefvrvgames

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class TriangleView: View{

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var currentColorIndex = 0
    private val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE)
    private val paint = Paint()
    private val path = Path()
    private val triangleSize = 100f
    private var centerX = 0f
    private var topY = 0f
    private val cornerRadius = 10f

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        centerX = (width / 2f) - 60f
        topY = height - 150f

        val colorTop = colors[currentColorIndex]
        val colorBottomLeft = colors[(currentColorIndex + 1) % colors.size]
        val colorBottomRight = colors[(currentColorIndex + 2) % colors.size]

        path.reset()
        path.moveTo(centerX + triangleSize / 2, topY)
        path.lineTo(centerX, topY + triangleSize)
        path.lineTo(centerX + triangleSize, topY + triangleSize)
        path.close()

        paint.color = colorTop
        //canvas.drawPath(path, paint)
        canvas.drawCircle(centerX + triangleSize / 2, topY, cornerRadius, paint)

        paint.color = colorBottomLeft
        canvas.drawCircle(centerX, topY + triangleSize, cornerRadius, paint)

        paint.color = colorBottomRight
        canvas.drawCircle(centerX + triangleSize, topY + triangleSize, cornerRadius, paint)
    }

    fun changeColor() {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        val targetAngle = -90f
        rotateCorner(targetAngle)
        //invalidate()
    }

    fun nextColor(){
        currentColorIndex = (currentColorIndex + 1) % colors.size
        val targetAngle = 90f
        rotateCorner(targetAngle)
        //invalidate()
    }

    fun previousColor(){
        currentColorIndex = if (currentColorIndex -1 < 0) colors.size -1 else currentColorIndex -1
        invalidate()
    }

    private fun rotateCorner(targetAngle: Float) {
        val animator = ValueAnimator.ofFloat(0f, targetAngle)
        animator.addUpdateListener { animation ->
            val angle = animation.animatedValue as Float
            rotate(angle)
            invalidate()
        }
        animator.duration = 500 // Adjust the duration as needed
        animator.start()
    }

    private fun rotate(angle: Float) {
        val radians = Math.toRadians(angle.toDouble())
        val newX = centerX + triangleSize / 2 + (triangleSize / 2 - cornerRadius) * cos(radians).toFloat()
        val newY = topY + triangleSize + (triangleSize / 2 - cornerRadius) * sin(radians).toFloat()
        path.reset()
        path.moveTo(newX, newY)
        path.lineTo(centerX, topY + triangleSize)
        path.lineTo(centerX + triangleSize, topY + triangleSize)
        path.close()
    }
}
