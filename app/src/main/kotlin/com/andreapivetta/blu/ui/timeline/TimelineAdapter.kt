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

    private val TYPE_ITEM = 1
    private val TYPE_ITEM_PHOTO = 2
    private val TYPE_ITEM_QUOTE = 3
    private val TYPE_ITEM_MULTIPLE_PHOTOS = 4
    private val TYPE_ITEM_VIDEO = 5
    private val TYPE_ITEM_LINK = 6

    var mDataSet: MutableList<Tweet> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? =
            when (viewType) {
                TYPE_ITEM -> StatusViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_basic, parent, false), listener)
                TYPE_ITEM_PHOTO -> StatusPhotoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_photo, parent, false), listener)
                TYPE_ITEM_QUOTE -> StatusQuoteViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_quote, parent, false), listener)
                TYPE_ITEM_MULTIPLE_PHOTOS -> StatusMultiplePhotosViewHolder(
                        LayoutInflater.from(parent?.context).inflate(
                                R.layout.tweet_multiplephotos, parent, false), listener)
                TYPE_ITEM_VIDEO -> StatusVideoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_video, parent, false), listener)
                TYPE_ITEM_LINK -> StatusLinkViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_link, parent, false), listener)
                else -> throw UnsupportedOperationException("No Type found")
            }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.setup(mDataSet[position])
    }

    override fun getItemCount() = mDataSet.size

    override fun getItemViewType(position: Int): Int {
        val tweet = mDataSet[position]
        when {
            tweet.hasSingleImage() -> return TYPE_ITEM_PHOTO
            tweet.hasSingleVideo() -> return TYPE_ITEM_VIDEO
            tweet.hasMultipleMedia() -> return TYPE_ITEM_MULTIPLE_PHOTOS
            tweet.quotedStatus -> return TYPE_ITEM_QUOTE
            tweet.hasLinks() -> return TYPE_ITEM_LINK
            else -> return TYPE_ITEM
        }
    }

}