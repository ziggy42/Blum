package com.andreapivetta.blu.ui.timeline.holders


import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.ImagesAdapter
import twitter4j.Status
import twitter4j.Twitter

class VHItemMultiplePhotos(container: View) : VHItem(container) {

    private val tweetPhotosRecyclerView: RecyclerView

    init {
        this.tweetPhotosRecyclerView = container.findViewById(R.id.tweetPhotosRecyclerView) as RecyclerView
        this.tweetPhotosRecyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
        this.tweetPhotosRecyclerView.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        this.tweetPhotosRecyclerView.setHasFixedSize(true)
    }

    override fun setup(status: Status, context: Context, favorites: MutableList<Long>,
                       retweets: MutableList<Long>, twitter: Twitter) {
        super.setup(status, context, favorites, retweets, twitter)
        tweetPhotosRecyclerView.adapter = ImagesAdapter(status.extendedMediaEntities, context)
    }
}
