package com.andreapivetta.blu.ui.timeline

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.holders.*
import twitter4j.ExtendedMediaEntity
import twitter4j.Status
import twitter4j.Twitter
import java.util.*

/**
 * Created by andrea on 17/05/16.
 */
class TimelineAdapter(var context: Context, var twitter: Twitter, var headerPosition: Int) :
        RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        private val TYPE_HEADER = 0
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
            TYPE_ITEM ->
                return VHItem(LayoutInflater.from(parent?.context).inflate(R.layout.tweet_basic, parent, false))
            TYPE_ITEM_PHOTO ->
                return VHItemPhoto(
                        LayoutInflater.from(parent?.context).inflate(R.layout.tweet_photo, parent, false))
            TYPE_ITEM_QUOTE ->
                VHItemQuote(
                        LayoutInflater.from(parent?.context).inflate(R.layout.tweet_quote, parent, false))
            TYPE_ITEM_MULTIPLE_PHOTOS ->
                return VHItemMultiplePhotos(
                        LayoutInflater.from(parent?.context).inflate(R.layout.tweet_multiplephotos, parent, false))
            TYPE_ITEM_VIDEO ->
                return VHItemVideo(
                        LayoutInflater.from(parent?.context).inflate(R.layout.tweet_video, parent, false))
            TYPE_HEADER ->
                return VHHeader(
                        LayoutInflater.from(parent?.context).inflate(R.layout.tweet_expanded, parent, false))
            else ->
                throw UnsupportedOperationException("No Type found")
        }

        // TODO ??
        return VHItem(LayoutInflater.from(parent?.context).inflate(R.layout.tweet_basic, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        holder?.setup(mDataSet[position], context, favorites, retweets, twitter)
    }

    override fun getItemCount(): Int = mDataSet.size

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position))
            return TYPE_HEADER

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

    private fun isPositionHeader(position: Int): Boolean {
        return position == headerPosition
    }

    private fun add(status: Status) {
        mDataSet.add(status)
        notifyItemInserted(mDataSet.size - 1)
    }
}