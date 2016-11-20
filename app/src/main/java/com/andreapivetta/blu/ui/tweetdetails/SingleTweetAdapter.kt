package com.andreapivetta.blu.ui.tweetdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.TimelineAdapter

/**
 * Created by andrea on 24/05/16.
 */
class SingleTweetAdapter(listener: DetailsInteractionListener) : TimelineAdapter(listener) {

    var headerIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            if (viewType == R.layout.tweet_big) StatusDetailsViewHolder(LayoutInflater.from(
                    parent?.context).inflate(R.layout.tweet_big, parent, false),
                    listener as DetailsInteractionListener)
            else super.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int) =
            if (position == headerIndex) R.layout.tweet_big else super.getItemViewType(position)

}