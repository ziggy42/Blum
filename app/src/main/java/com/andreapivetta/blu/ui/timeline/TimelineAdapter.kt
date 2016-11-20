package com.andreapivetta.blu.ui.timeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.holders.*
import java.util.*

/**
 * Created by andrea on 17/05/16.
 */
open class TimelineAdapter(val listener: InteractionListener) :
        RecyclerView.Adapter<BaseViewHolder>() {

    var tweets: MutableList<Tweet> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? =
            when (viewType) {
                R.layout.tweet_basic -> StatusViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_basic, parent, false), listener)
                R.layout.tweet_photo -> StatusPhotoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_photo, parent, false), listener)
                R.layout.tweet_quote -> StatusQuoteViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_quote, parent, false), listener)
                R.layout.tweet_multiplephotos -> StatusMultiplePhotosViewHolder(
                        LayoutInflater.from(parent?.context).inflate(
                                R.layout.tweet_multiplephotos, parent, false), listener)
                R.layout.tweet_video -> StatusVideoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_video, parent, false), listener)
                R.layout.tweet_link -> StatusLinkViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_link, parent, false), listener)
                else -> throw UnsupportedOperationException("No Type found")
            }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.setup(tweets[position])
    }

    override fun getItemCount() = tweets.size

    override fun getItemViewType(position: Int): Int {
        val tweet = tweets[position]
        when {
            tweet.hasSingleImage() -> return R.layout.tweet_photo
            tweet.hasSingleVideo() -> return R.layout.tweet_video
            tweet.hasMultipleMedia() -> return R.layout.tweet_multiplephotos
            tweet.quotedStatus -> return R.layout.tweet_quote
            tweet.hasLinks() -> return R.layout.tweet_link
            else -> return R.layout.tweet_basic
        }
    }

}