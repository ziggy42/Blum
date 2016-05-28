package com.andreapivetta.blu.ui.tweetdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TimelineAdapter
import com.andreapivetta.blu.ui.timeline.holders.BaseViewHolder

/**
 * Created by andrea on 24/05/16.
 */
class SingleTweetAdapter(listener: InteractionListener) : TimelineAdapter(listener) {

    companion object {
        private val TYPE_HEADER = 0
    }

    var currentIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder? {
        if (viewType == TYPE_HEADER)
            return StatusDetailsViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.tweet_big, parent, false), listener, this)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == currentIndex)
            return TYPE_HEADER
        return super.getItemViewType(position)
    }

}