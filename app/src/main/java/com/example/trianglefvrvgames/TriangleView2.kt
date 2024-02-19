package com.example.trianglefvrvgames

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class TriangleView2 : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var currentColorIndex = 0
    private val colors =
        arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.GRAY, Color.YELLOW, Color.MAGENTA)
    private val paint = Paint()
    private val line = Paint()
    private val path = Path()
    private val triangleSize = 100f
    private var centerX = 0f
    private var topY = 0f
    private val cornerRadius = 32f
    private var lineY = 100f
    private var lineAnimator: ValueAnimator? = null
    private var totalPoints = 0

    interface GameInterface {
        fun stop()
        fun pause()
        fun resume()
        fun points(points: String)
    }

    public var gameInterface: GameInterface? = null

    init {
        line.apply {
            strokeWidth = 20f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        centerX = (width / 2f) - 60f
        topY = height - 150f

        path.reset()
        path.moveTo(centerX + triangleSize / 2, topY)
        path.lineTo(centerX, topY + triangleSize)
        path.lineTo(centerX + triangleSize, topY + triangleSize)
        path.close()

        //Horizontal Line
        val rectF = RectF(20f, lineY - cornerRadius, width - 20f, lineY + cornerRadius)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, line)

        Log.d("TEST_INDEX", "Index : $currentColorIndex")

        paint.color = colors[currentColorIndex]
        canvas.drawCircle(centerX + triangleSize / 2, topY, cornerRadius, paint)

        paint.color = colors[(currentColorIndex + 1) % colors.size]
        canvas.drawCircle(centerX, topY + triangleSize - 60, cornerRadius, paint)

        paint.color = colors[(currentColorIndex + 2) % colors.size]
        canvas.drawCircle(centerX, topY + triangleSize, cornerRadius, paint)

        paint.color = colors[(currentColorIndex + 3) % colors.size]
        canvas.drawCircle(
            centerX + triangleSize - 50,
            topY + triangleSize + 40,
            cornerRadius,
            paint
        )

        paint.color = colors[(currentColorIndex + 4) % colors.size]
        canvas.drawCircle(centerX + triangleSize, topY + triangleSize, cornerRadius, paint)

        paint.color = colors[(currentColorIndex + 5) % colors.size]
        canvas.drawCircle(centerX + triangleSize, topY + triangleSize - 60, cornerRadius, paint)

    }

    fun changeColor() {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        invalidate()
    }

    fun nextColor() {
        currentColorIndex =
            if (currentColorIndex + 1 > colors.size - 1) 0 else currentColorIndex + 1
        val targetAngle = 30f
        rotateCorner(targetAngle)
    }

    fun previousColor() {
        currentColorIndex =
            if (currentColorIndex - 1 < 0) colors.size - 1 else currentColorIndex - 1
        val targetAngle = -30f
        rotateCorner(targetAngle)
    }

    private fun animateLine() {
        lineY = 100f
        line.color = colors[Random.nextInt(colors.size)]
        lineAnimator = ValueAnimator.ofFloat(lineY, height - 150f)
        lineAnimator?.addUpdateListener { animation ->
            lineY = animation.animatedValue as Float
            invalidate()
            if (colors[currentColorIndex] == line.color && lineY == (height - 150f)) {
                totalPoints++
                Log.d("TEST_POINTS", "start points: $totalPoints")
                gameInterface?.points(totalPoints.toString())
                start()
            } else {
                if (lineY == (height - 150f)) {
                    totalPoints = 0
                    Log.d("TEST_POINTS", "stop points: $totalPoints")
                    stop()
                }
            }
        }
        lineAnimator?.interpolator = AccelerateDecelerateInterpolator()//LinearInterpolator()//DecelerateInterpolator() //AccelerateDecelerateInterpolator()
        val level = if (totalPoints in 0..9) {
            2300L
        } else if (totalPoints in 10..19) {
            2100L
        } else if (totalPoints in 20..29) {
            1900L
        } else if (totalPoints in 30..39) {
            1700L
        } else if (totalPoints in 40..49) {
            1500L
        } else if (totalPoints in 50..59) {
            1300L
        } else if (totalPoints in 60..69) {
            1100L
        } else if (totalPoints in 70..79) {
            900L
        } else if (totalPoints in 80..89) {
            800L
        } else if (totalPoints in 90..99) {
            600L
        } else 2500L
        Log.d("TEST_LEVEL", "stop points: $level")
        lineAnimator?.duration = level // Adjust the duration as needed
        lineAnimator?.start()
    }

    private fun rotateCorner(targetAngle: Float) {
        val animator = ValueAnimator.ofFloat(0f, targetAngle)
        animator.addUpdateListener { animation ->
            val angle = animation.animatedValue as Float
            rotate(angle)
            invalidate()
        }
        animator.interpolator = DecelerateInterpolator()
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

    fun start() {
        animateLine()
    }

    fun pause() {
        lineAnimator?.pause()
        gameInterface?.pause()
    }

    fun stop() {
        invalidate()
        gameInterface?.stop()
    }
}
