package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.bumptech.glide.Glide
import twitter4j.Status

class VHItemPhoto(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        VHItem(container, listener, tweetInfoProvider) {

    private val tweetPhotoImageView: ImageView

    init {
        this.tweetPhotoImageView = container.findViewById(R.id.tweetPhotoImageView) as ImageView
    }

    override fun setup(status: Status) {
        super.setup(status)

        val mediaEntity = status.mediaEntities[0]
        if (mediaEntity.type == "photo") {
            Glide.with(container.context)
                    .load(mediaEntity.mediaURL)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .into(tweetPhotoImageView)

            tweetPhotoImageView.setOnClickListener { listener.showImage(mediaEntity.mediaURL) }
        }
    }
}
