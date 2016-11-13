package com.andreapivetta.blu.ui.mediatimeline.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.andreapivetta.blu.R

/**
 * Created by andrea on 24/06/16.
 */
class MediaViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

    val mediaImageView = container.findViewById(R.id.imageView) as ImageView
    val favouriteImageButton = container.findViewById(R.id.favouriteImageButton) as ImageButton
    val retweetImageButton = container.findViewById(R.id.retweetImageButton) as ImageButton

}