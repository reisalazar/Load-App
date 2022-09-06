package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textWidth: Float by lazy { paint.measureText(R.string.button_loading.toString()) }
    private var textSize: Float = resources.getDimension(R.dimen.default_text_size)

    private var widthSize = 0
    private var heightSize = 0

    private var buttonColorBackground = resources.getColor(R.color.colorPrimary)
    private var buttonLoadColor = resources.getColor(R.color.colorPrimaryDark)
    private var buttonText = resources.getString(R.string.button_completed)
    private var progressButton = 0f
    private var circleXOffset = textSize / 2


    private var circleColor = resources.getColor(R.color.colorAccent)
    private var progressCircle = 0f

    private var valueAnimator = ValueAnimator()


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->

        when (new) {
            ButtonState.Clicked -> {
                changeButtonState(ButtonState.Loading)
                invalidate()
            }

            ButtonState.Loading -> {
                buttonText = context.resources.getString(R.string.button_loading)
                buttonAnimate()
            }

            ButtonState.Completed -> {
                valueAnimator.cancel()
                buttonText = resources.getString(R.string.button_completed)
                invalidate()
            }

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawButton(it)
            drawButtonLoading(it)
            drawText(it)
            drawCircleLoading(it)
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    private fun drawCircleLoading(canvas: Canvas) {
        canvas.save()
        canvas.translate(
            widthSize / 2 + textWidth / 2 + circleXOffset,
            heightSize / 2 - textSize / 2
        )
        paint.color = circleColor
        canvas.drawArc(RectF(0f, 0f, textSize, textSize), 0F, progressCircle * 0.365f, true, paint)
        canvas.restore()
    }

    private fun drawButtonLoading(canvas: Canvas) {
        // loading button
        paint.color = buttonLoadColor
        canvas.drawRect(0f, 0f, widthSize * progressButton / 360f, heightSize.toFloat(), paint)
    }

    private fun drawButton(canvas: Canvas) {
        paint.color = buttonColorBackground
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.color = Color.WHITE
        canvas.drawText(buttonText, widthSize / 2.0f, heightSize / 2.0f + 20.0f, paint)
    }

    fun changeButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun buttonAnimate() {
        buttonText = resources.getString(R.string.button_loading)
        valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
        valueAnimator.duration = 4000

        valueAnimator.addUpdateListener { animation ->
            progressButton = animation.animatedValue as Float
            progressCircle = (widthSize.toFloat() / 365) * progressButton
            invalidate()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                progressButton = 0f
                progressCircle = 0f
            }
        })
        valueAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}