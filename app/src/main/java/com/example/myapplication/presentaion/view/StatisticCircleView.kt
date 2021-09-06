package com.example.myapplication.presentaion.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.example.myapplication.R

class StatisticCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    @ColorInt
    private var rightColor: Int

    @ColorInt
    private var wrongColor: Int

    @ColorInt
    private var notKnowColor: Int

    private var rectCircle = RectF()

    private var rectLegend = RectF()

    var rightAnswerPercent: Float = 0F
        set(value) {
            field = value
            invalidate()
        }
    var wrongAnswerPercent: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    var notAnswerPercent: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var text: String
    private val acceptableText: String
    private val goodText: String
    private val fineText: String
    private val badText: String
    private var paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 80f

    }
    private var paintTextStatistic = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = paintText.textSize / 2

    }
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeWidth = 64f
    }
    private val textBound = Rect()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StaticCircleView,
            R.attr.staticCircleViewDefaultAttr, R.style.StaticCircleViewDefaultStyle
        ).apply {
            try {
                rightColor = getColor(R.styleable.StaticCircleView_rightColor, Color.GREEN)
                wrongColor = getColor(R.styleable.StaticCircleView_wrongColor, Color.RED)
                notKnowColor = getColor(R.styleable.StaticCircleView_notAnswerColor, Color.GRAY)
                rightAnswerPercent = getFloat(R.styleable.StaticCircleView_right, 75F)
                wrongAnswerPercent = getFloat(R.styleable.StaticCircleView_wrong, 25F)
                notAnswerPercent = getFloat(R.styleable.StaticCircleView_notAnswer, 0F)
                rightAnswerPercent /= 100f
                wrongAnswerPercent /= 100f
                notAnswerPercent /= 100f
                badText = getString(R.styleable.StaticCircleView_badResultText).toString()
                acceptableText =
                    getString(R.styleable.StaticCircleView_acceptableResultText).toString()
                goodText = getString(R.styleable.StaticCircleView_goodResultText).toString()
                fineText = getString(R.styleable.StaticCircleView_fineResultText).toString()
                text = acceptableText
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var minWidth = 0f
        var minHeight = 0f
        paintText.textBounds(text)
        minHeight /= 2f
        minWidth += paint.strokeWidth + textBound.width()
        minHeight += paint.strokeWidth
        val requestedWidth = Math.max(minWidth.toInt(), measuredWidth)
        val requestedHeight = Math.max(minHeight.toInt(), measuredHeight)
        val requestSize = Math.max(requestedHeight, requestedWidth)
        setMeasuredDimension(
            resolveSizeAndState(requestSize, widthMeasureSpec, 0),
            resolveSizeAndState(requestSize, heightMeasureSpec, 0)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            text = badText
            if (rightAnswerPercent >= 0.5f) {
                text = acceptableText
            }
            if (rightAnswerPercent >= 0.75f) {
                text = goodText
            }
            if (rightAnswerPercent >= 0.85f) {
                text = fineText
            }
            paintText.textBounds(text)
            paint.color = rightColor
            it.drawArc(rectCircle, -180f, 360f * rightAnswerPercent, false, paint)
            paint.color = wrongColor
            it.drawArc(
                rectCircle,
                -180f + 360f * rightAnswerPercent,
                360f * wrongAnswerPercent,
                false,
                paint
            )
            paint.setColor(notKnowColor)
            it.drawArc(
                rectCircle,
                -180f + 360f * (rightAnswerPercent + wrongAnswerPercent),
                360f * notAnswerPercent,
                false,
                paint
            )
            it.drawText(
                text,
                rectCircle.centerX() - textBound.width() / 2,
                rectCircle.centerY() + textBound.height() / 2,
                paintText
            )

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val size = Math.min(w, h) - paint.strokeWidth / 2
        rectCircle.set(
            paint.strokeWidth / 2 + paddingLeft,
            paint.strokeWidth / 2 + paddingTop,
            size - paddingRight,
            size - paddingBottom
        )

        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun Paint.textBounds(s: String) = getTextBounds(s, 0, s.length, textBound)
}