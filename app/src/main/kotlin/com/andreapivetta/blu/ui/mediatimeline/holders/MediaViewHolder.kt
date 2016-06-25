package com.andreapivetta.blu.ui.mediatimeline.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R

/**
 * Created by andrea on 24/06/16.
 */
class MediaViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

    var mediaImageView: ImageView

    init {
        mediaImageView = container.findViewById(R.id.imageView) as ImageView
    }

}