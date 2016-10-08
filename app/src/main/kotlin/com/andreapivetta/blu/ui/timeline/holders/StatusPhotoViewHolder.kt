package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import com.andreapivetta.blu.common.utils.loadUrlCenterCrop
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import kotlinx.android.synthetic.main.tweet_photo.view.*

class StatusPhotoViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetPhotoImageView = container.tweetPhotoImageView

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        if (tweet.hasSingleImage()) {
            tweetPhotoImageView.loadUrlCenterCrop(tweet.getImageUrl())
            tweetPhotoImageView.setOnClickListener { listener.showImage(tweet.getImageUrl()) }
        }
    }
}
