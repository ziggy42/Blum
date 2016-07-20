package com.andreapivetta.blu.ui.timeline.holders


import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide

class StatusVideoViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetVideoImageView: ImageView
    private val playVideoImageButton: ImageButton

    init {
        tweetVideoImageView = container.findViewById(R.id.tweetVideoImageView) as ImageView
        playVideoImageButton = container.findViewById(R.id.playVideoImageButton) as ImageButton
    }

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
