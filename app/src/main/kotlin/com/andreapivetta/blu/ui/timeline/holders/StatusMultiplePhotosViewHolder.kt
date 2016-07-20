package com.andreapivetta.blu.ui.timeline.holders


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener

class StatusMultiplePhotosViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetPhotosRecyclerView = container.findViewById(R.id.tweetPhotosRecyclerView)
            as RecyclerView

    init {
        tweetPhotosRecyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
        tweetPhotosRecyclerView.layoutManager =
                LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        tweetPhotosRecyclerView.setHasFixedSize(true)
    }

    override fun setup(tweet: Tweet) {
        super.setup(tweet)
        tweetPhotosRecyclerView.adapter = ImagesAdapter(tweet.mediaEntities, listener)
    }
}
