package com.andreapivetta.blu.ui.timeline.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener

abstract class BaseViewHolder(val container: View, val listener: InteractionListener) :
        RecyclerView.ViewHolder(container) {

    protected var userNameTextView = container.findViewById(R.id.userNameTextView) as TextView
    protected var userScreenNameTextView = container.findViewById(R.id.userScreenNameTextView) as TextView
    protected var statusTextView = container.findViewById(R.id.statusTextView) as TextView
    protected var timeTextView = container.findViewById(R.id.timeTextView) as TextView
    protected val retweetsStatsTextView = container.findViewById(R.id.retweetsStatsTextView) as TextView
    protected val favouritesStatsTextView = container.findViewById(R.id.favouritesStatsTextView) as TextView
    protected var userProfilePicImageView = container.findViewById(R.id.userProfilePicImageView) as ImageView
    protected var favouriteImageButton = container.findViewById(R.id.favouriteImageButton) as ImageButton
    protected var retweetImageButton = container.findViewById(R.id.retweetImageButton) as ImageButton
    protected var respondImageButton = container.findViewById(R.id.respondImageButton) as ImageButton

    abstract fun setup(tweet: Tweet)

}
