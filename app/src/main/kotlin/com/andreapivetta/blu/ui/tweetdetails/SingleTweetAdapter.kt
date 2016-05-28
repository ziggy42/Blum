package com.andreapivetta.blu.ui.tweetdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TimelineAdapter

/**
 * Created by andrea on 24/05/16.
 */
class SingleTweetAdapter(listener: InteractionListener) : TimelineAdapter(listener) {

    companion object {
        private val TYPE_HEADER = 0
    }

    var currentIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            if (viewType == TYPE_HEADER) StatusDetailsViewHolder(LayoutInflater.from(
                    parent?.context).inflate(R.layout.tweet_big, parent, false), listener, this)
            else super.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int) =
            if (position == currentIndex) TYPE_HEADER else super.getItemViewType(position)
}