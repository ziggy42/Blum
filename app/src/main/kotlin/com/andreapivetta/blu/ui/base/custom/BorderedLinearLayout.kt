package com.andreapivetta.blu.ui.base.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils


class BorderedLinearLayout : LinearLayout {

    private var strokePaint: Paint? = null
    private val r = Rect()
    private val outline = RectF()
    private val radius = Utils.dpToPx(context, 3).toFloat()

    constructor(context: Context) : super(context) {
        setWillNotDraw(false)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setWillNotDraw(false)
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attrs, R.styleable.BorderedLinearLayout, 0, 0)

        strokePaint = Paint()
        strokePaint!!.style = Paint.Style.STROKE
        strokePaint!!.color = a.getColor(R.styleable.BorderedLinearLayout_borderColor, Color.BLACK)
        strokePaint!!.strokeWidth = 2f
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.getClipBounds(r)
        outline.set(1f, 1f, (r.right - 1).toFloat(), (r.bottom - 1).toFloat())
        canvas.drawRoundRect(outline, radius, radius, strokePaint!!)
    }
}
