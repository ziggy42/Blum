package com.andreapivetta.blu.ui.base.custom.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by andrea on 16/11/16.
 */
class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.bottom = space
    }
}
