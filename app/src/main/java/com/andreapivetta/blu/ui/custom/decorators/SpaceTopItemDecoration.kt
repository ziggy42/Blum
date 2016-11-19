package com.andreapivetta.blu.ui.custom.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class SpaceTopItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.bottom = space

        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = space
    }
}
