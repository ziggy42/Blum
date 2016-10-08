package com.andreapivetta.blu.ui.timeline.holders


import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_cover.view.*

class StatusVideoViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetVideoImageView = container.tweetVideoImageView
    private val playVideoImageButton = container.playVideoImageButton

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        Glide.with(container.context)
                .load(tweet.getVideoCoverUrl())
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(tweetVideoImageView)

        playVideoImageButton.setOnClickListener {
            val pair = tweet.getVideoUrlType()
            listener.showVideo(pair.first, pair.second)
        }
    }

}
