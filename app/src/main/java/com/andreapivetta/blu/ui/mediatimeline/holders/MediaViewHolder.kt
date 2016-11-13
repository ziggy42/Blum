package com.andreapivetta.blu.ui.mediatimeline.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.media.view.*

/**
 * Created by andrea on 24/06/16.
 */
class MediaViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

    val mediaImageView = container.imageView
    val favouriteImageButton = container.favouriteImageButton
    val retweetImageButton = container.retweetImageButton

}