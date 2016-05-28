package com.andreapivetta.blu.ui.timeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.holders.*
import twitter4j.ExtendedMediaEntity
import twitter4j.Status
import java.util.*

/**
 * Created by andrea on 17/05/16.
 */
open class TimelineAdapter(val listener: InteractionListener) :
        RecyclerView.Adapter<BaseViewHolder>(), TweetInfoProvider {

    companion object {
        private val TYPE_ITEM = 1
        private val TYPE_ITEM_PHOTO = 2
        private val TYPE_ITEM_QUOTE = 3
        private val TYPE_ITEM_MULTIPLE_PHOTOS = 4
        private val TYPE_ITEM_VIDEO = 5
    }

    var mDataSet: MutableList<Status> = ArrayList()
    private val favorites: MutableList<Long> = ArrayList()
    private val retweets: MutableList<Long> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? {
        when (viewType) {
            TYPE_ITEM -> return StatusViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_basic, parent, false), listener, this)
            TYPE_ITEM_PHOTO -> return StatusPhotoViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_photo, parent, false), listener, this)
            TYPE_ITEM_QUOTE -> return StatusQuoteViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_quote, parent, false), listener, this)
            TYPE_ITEM_MULTIPLE_PHOTOS -> return StatusMultiplePhotosViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_multiplephotos, parent, false), listener, this)
            TYPE_ITEM_VIDEO -> return StatusVideoViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_video, parent, false), listener, this)
            else -> throw UnsupportedOperationException("No Type found")
        }

        return StatusViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.tweet_basic, parent, false), listener, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.setup(mDataSet[position])
    }

    override fun getItemCount(): Int = mDataSet.size

    override fun getItemViewType(position: Int): Int {
        val status = mDataSet[position]
        val mediaEntities: Array<ExtendedMediaEntity> = status.extendedMediaEntities
        if (mediaEntities.size == 1) {
            if ("photo".equals(mediaEntities[0].type))
                return TYPE_ITEM_PHOTO
            return TYPE_ITEM_VIDEO
        }

        if (mediaEntities.size > 1)
            return TYPE_ITEM_MULTIPLE_PHOTOS

        if (status.quotedStatusId > 0)
            return TYPE_ITEM_QUOTE

        return TYPE_ITEM
    }

    fun setFavorite(statusId: Long) {
        favorites.add(statusId)
    }

    fun setRetweet(statusId: Long) {
        retweets.add(statusId)
    }

    fun removeFavorite(statusId: Long) {
        favorites.remove(statusId)
    }

    fun removeRetweet(statusId: Long) {
        retweets.remove(statusId)
    }

    // TweetInfoProvider
    override fun isFavorite(status: Status) = favorites.contains(status.id)

    override fun isRetweet(status: Status) = retweets.contains(status.id)
}