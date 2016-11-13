package com.andreapivetta.blu.ui.timeline.holders


import android.view.View
import com.andreapivetta.blu.common.utils.loadUrlCenterCrop
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import kotlinx.android.synthetic.main.video_cover.view.*

class StatusVideoViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetVideoImageView = container.tweetVideoImageView
    private val playVideoImageButton = container.playVideoImageButton

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        tweetVideoImageView.loadUrlCenterCrop(tweet.getVideoCoverUrl())

        playVideoImageButton.setOnClickListener {
            val pair = tweet.getVideoUrlType()
            listener.showVideo(pair.first, pair.second)
        }
    }

}
