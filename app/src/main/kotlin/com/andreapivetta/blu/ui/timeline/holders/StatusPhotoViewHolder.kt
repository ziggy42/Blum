package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide

class StatusPhotoViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetPhotoImageView = container.findViewById(R.id.tweetPhotoImageView) as ImageView

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        if (tweet.hasSingleImage()) {
            Glide.with(container.context)
                    .load(tweet.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .into(tweetPhotoImageView)

            tweetPhotoImageView.setOnClickListener { listener.showImage(tweet.getImageUrl()) }
        }
    }
}
