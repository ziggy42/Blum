package com.andreapivetta.blu.ui.timeline.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import twitter4j.Status
import twitter4j.Twitter

abstract class BaseViewHolder(container: View) : RecyclerView.ViewHolder(container) {

    protected var userNameTextView: TextView
    protected var userScreenNameTextView: TextView
    protected var statusTextView: TextView
    protected var timeTextView: TextView
    protected var retweetTextView: TextView
    protected val retweetsStatsTextView: TextView
    protected val favouritesStatsTextView: TextView
    protected var userProfilePicImageView: ImageView
    protected var favouriteImageButton: ImageButton
    protected var retweetImageButton: ImageButton
    protected var respondImageButton: ImageButton

    init {
        this.userNameTextView = container.findViewById(R.id.userNameTextView) as TextView
        this.userScreenNameTextView = container.findViewById(R.id.userScreenName_TextView) as TextView
        this.statusTextView = container.findViewById(R.id.statusTextView) as TextView
        this.userProfilePicImageView = container.findViewById(R.id.userProfilePicImageView) as ImageView
        this.timeTextView = container.findViewById(R.id.timeTextView) as TextView
        this.retweetTextView = container.findViewById(R.id.retweetTextView) as TextView
        this.retweetsStatsTextView = container.findViewById(R.id.retweetsStatsTextView) as TextView
        this.favouritesStatsTextView = container.findViewById(R.id.favouritesStatsTextView) as TextView
        this.favouriteImageButton = container.findViewById(R.id.favouriteImageButton) as ImageButton
        this.retweetImageButton = container.findViewById(R.id.retweetImageButton) as ImageButton
        this.respondImageButton = container.findViewById(R.id.respondImageButton) as ImageButton
    }

    abstract fun setup(status: Status, context: Context, favorites: MutableList<Long>,
                       retweets: MutableList<Long>, twitter: Twitter)

}
