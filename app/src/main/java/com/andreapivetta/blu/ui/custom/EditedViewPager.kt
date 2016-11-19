package com.andreapivetta.blu.ui.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by andrea on 28/05/16.
 */
class EditedViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onTouchEvent(ev: MotionEvent?) = try {
        super.onTouchEvent(ev)
    } catch(err: IllegalArgumentException) {
        err.printStackTrace()
        false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) = try {
        super.onInterceptTouchEvent(ev)
    } catch(err: IllegalArgumentException) {
        err.printStackTrace()
        false
    }

}