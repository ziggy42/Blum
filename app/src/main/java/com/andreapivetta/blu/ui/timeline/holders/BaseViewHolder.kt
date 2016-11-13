package com.andreapivetta.blu.ui.timeline.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import kotlinx.android.synthetic.main.item_interaction.view.*
import kotlinx.android.synthetic.main.item_userinfo.view.*
import kotlinx.android.synthetic.main.tweet_basic.view.*

abstract class BaseViewHolder(val container: View, val listener: InteractionListener) :
        RecyclerView.ViewHolder(container) {

    protected var userNameTextView = container.userNameTextView
    protected var userScreenNameTextView = container.userScreenNameTextView
    protected var statusTextView = container.statusTextView
    protected var timeTextView = container.timeTextView
    protected val retweetsStatsTextView = container.retweetsStatsTextView
    protected val favouritesStatsTextView = container.favouritesStatsTextView
    protected var userProfilePicImageView = container.userProfilePicImageView
    protected var favouriteImageButton = container.favouriteImageButton
    protected var retweetImageButton = container.retweetImageButton
    protected var respondImageButton = container.respondImageButton

    abstract fun setup(tweet: Tweet)

}
