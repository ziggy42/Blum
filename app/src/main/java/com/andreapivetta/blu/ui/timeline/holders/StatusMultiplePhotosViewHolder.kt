package com.andreapivetta.blu.ui.timeline.holders


import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.base.custom.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener
import kotlinx.android.synthetic.main.tweet_multiplephotos.view.*

class StatusMultiplePhotosViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val tweetPhotosRecyclerView = container.tweetPhotosRecyclerView

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
