package com.andreapivetta.blu.ui.timeline.holders


import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

import com.andreapivetta.blu.R
import com.bumptech.glide.Glide

import java.util.ArrayList

import twitter4j.ExtendedMediaEntity
import twitter4j.Status
import twitter4j.Twitter

class VHItemVideo(container: View) : VHItem(container) {

    private val tweetVideoImageView: ImageView
    private val playVideoImageButton: ImageButton

    init {
        this.tweetVideoImageView = container.findViewById(R.id.tweetVideoImageView) as ImageView
        this.playVideoImageButton = container.findViewById(R.id.playVideoImageButton) as ImageButton
    }

    override fun setup(status: Status, context: Context, favorites: MutableList<Long>, retweets: MutableList<Long>, twitter: Twitter) {
        super.setup(status, context, favorites, retweets, twitter)

        val mediaEntity = status.extendedMediaEntities[0]

        Glide.with(context).load(mediaEntity.mediaURL).placeholder(R.drawable.placeholder).centerCrop().into(tweetVideoImageView)

        playVideoImageButton.setOnClickListener { /* TODO */ }
    }


}
