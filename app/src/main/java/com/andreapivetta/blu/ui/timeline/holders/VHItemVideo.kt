package com.andreapivetta.blu.ui.timeline.holders


import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.bumptech.glide.Glide
import twitter4j.Status

class VHItemVideo(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        VHItem(container, listener, tweetInfoProvider) {

    private val tweetVideoImageView: ImageView
    private val playVideoImageButton: ImageButton

    init {
        this.tweetVideoImageView = container.findViewById(R.id.tweetVideoImageView) as ImageView
        this.playVideoImageButton = container.findViewById(R.id.playVideoImageButton) as ImageButton
    }

    override fun setup(status: Status) {
        super.setup(status)

        val mediaEntity = status.extendedMediaEntities[0]

        Glide.with(container.context).load(mediaEntity.mediaURL).placeholder(R.drawable.placeholder).centerCrop().into(tweetVideoImageView)

        playVideoImageButton.setOnClickListener { /* TODO */ }
    }


}
