package com.andreapivetta.blu.ui.timeline.holders


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import twitter4j.Status

class StatusMultiplePhotosViewHolder(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        StatusViewHolder(container, listener, tweetInfoProvider) {

    private val tweetPhotosRecyclerView: RecyclerView

    init {
        this.tweetPhotosRecyclerView = container.findViewById(R.id.tweetPhotosRecyclerView) as RecyclerView
        this.tweetPhotosRecyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
        this.tweetPhotosRecyclerView.layoutManager =
                LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        this.tweetPhotosRecyclerView.setHasFixedSize(true)
    }

    override fun setup(status: Status) {
        super.setup(status)
        tweetPhotosRecyclerView.adapter = ImagesAdapter(status.extendedMediaEntities, listener)
    }
}
