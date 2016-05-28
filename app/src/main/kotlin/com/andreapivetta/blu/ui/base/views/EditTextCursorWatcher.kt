package com.andreapivetta.blu.ui.base.views

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by andrea on 20/05/16.
 */
class EditTextCursorWatcher : EditText {

    private var cursorWatcher: CursorWatcher? = null
    private var lastStartCursorPosition = 1

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (cursorWatcher != null) {
            if (lastStartCursorPosition < selStart - 1 || lastStartCursorPosition > selStart + 1)
                cursorWatcher!!.onCursorPositionChanged(selStart, selEnd)
            lastStartCursorPosition = selStart
        }
    }

    fun addCursorWatcher(cursorWatcher: CursorWatcher) {
        this.cursorWatcher = cursorWatcher
    }

    interface CursorWatcher {
        fun onCursorPositionChanged(currentStartPosition: Int, currentEndPosition: Int)
    }
}