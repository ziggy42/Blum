package com.andreapivetta.blu.ui.timeline.holders

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide
import twitter4j.Status
import twitter4j.Twitter
import java.util.*

class VHItemPhoto(container: View) : VHItem(container) {

    private val tweetPhotoImageView: ImageView

    init {
        this.tweetPhotoImageView = container.findViewById(R.id.tweetPhotoImageView) as ImageView
    }

    override fun setup(status: Status, context: Context, favorites: MutableList<Long>,
                       retweets: MutableList<Long>, twitter: Twitter) {
        super.setup(status, context, favorites, retweets, twitter)

        val mediaEntity = status.mediaEntities[0]
        if (mediaEntity.type == "photo") {
            Glide.with(context).load(mediaEntity.mediaURL).placeholder(R.drawable.placeholder).centerCrop().into(tweetPhotoImageView)

            tweetPhotoImageView.setOnClickListener { /* TODO */ }
        }
    }
}
