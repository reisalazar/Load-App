package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var buttonColorBackground = resources.getColor(R.color.colorPrimary)
    private var buttonLoadColor = resources.getColor(R.color.colorPrimaryDark)
    private var buttonText = context.resources.getString(R.string.button_name)
    private var buttonTextColor = 0

    private var circleColor = resources.getColor(R.color.colorAccent)
    private val circleRadius = 30.0f
    private val circleDiameter = circleRadius * 2
    private var progress = 0f


    private val valueAnimator = ValueAnimator()


    private val buttonPaint = Paint().apply {
        isAntiAlias = true
        color = buttonColorBackground
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Clicked -> {
                buttonState = (ButtonState.Loading)
            }

            ButtonState.Loading -> {
                buttonText = context.resources.getString(R.string.button_loading)
                valueAnimator.start()

            }

            ButtonState.Completed -> {
                buttonText = context.resources.getString(R.string.button_name)
                valueAnimator.cancel()
            }

        }
    }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.LoadingButton).apply {
            buttonColorBackground = getColor(R.styleable.LoadingButton_Color, 0)
            buttonLoadColor = getColor(R.styleable.LoadingButton_LoadColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_CircleColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawLoadingButton(canvas)
            drawButtonColor(canvas)
            drawText(canvas)
            drawCircleLoading(canvas)
        }

    }

    private fun drawLoadingButton(canvas: Canvas) {
        // loading button
        buttonPaint.color = buttonColorBackground
        canvas.drawRect(0f, 0f, widthSize * 0 / 360f, heightSize.toFloat(), buttonPaint)
    }

    private fun drawButtonColor(canvas: Canvas) {
        buttonPaint.color = buttonColorBackground
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), buttonPaint)
    }

    private fun drawText(canvas: Canvas) {
        buttonPaint.color = buttonTextColor
        canvas.drawText(
            buttonText,
            widthSize.toFloat() / 2,
            heightSize.toFloat() / 2 + 30,
            buttonPaint
        )
//        buttonPaint.color = buttonTextColor
//        canvas.drawText(buttonLabel, widthSize / 2.0f, heightSize / 2.0f + 30.0f, buttonPaint)
    }

    private fun drawCircleLoading(canvas: Canvas) {
        buttonPaint.color = getColor(context, R.color.colorAccent)
        canvas.drawArc(
            0.75f * widthSize, heightSize * 0.3f, 0.75f * widthSize + circleDiameter,
            heightSize * 0.3f + circleDiameter, 0f, 360f * 0, true, buttonPaint
        )
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