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

    companion object {
        private val TYPE_ITEM = 1
        private val TYPE_ITEM_PHOTO = 2
        private val TYPE_ITEM_QUOTE = 3
        private val TYPE_ITEM_MULTIPLE_PHOTOS = 4
        private val TYPE_ITEM_VIDEO = 5
    }

    var mDataSet: MutableList<Tweet> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? {
        when (viewType) {
            TYPE_ITEM -> return StatusViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_basic, parent, false), listener)
            TYPE_ITEM_PHOTO -> return StatusPhotoViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_photo, parent, false), listener)
            TYPE_ITEM_QUOTE -> return StatusQuoteViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_quote, parent, false), listener)
            TYPE_ITEM_MULTIPLE_PHOTOS -> return StatusMultiplePhotosViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.tweet_multiplephotos, parent, false), listener)
            TYPE_ITEM_VIDEO -> return StatusVideoViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_video, parent, false), listener)
            else -> throw UnsupportedOperationException("No Type found")
        }
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
            else -> return TYPE_ITEM
        }
    }

}