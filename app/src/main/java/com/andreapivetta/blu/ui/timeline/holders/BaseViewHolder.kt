package com.andreapivetta.blu.ui.timeline.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import twitter4j.Status
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseViewHolder(val container: View, val listener: InteractionListener,
                              val tweetInfoProvider: TweetInfoProvider) :
        RecyclerView.ViewHolder(container) {

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

    abstract fun setup(status: Status)

    protected fun formatDate(date: Date, context: Context): String {
        val c = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c2.time = date

        val diff = c.timeInMillis - c2.timeInMillis
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds > 60) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            if (minutes > 60) {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours > 24) {
                    if (c.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                        return " • " + java.text.SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
                    else
                        return " • " + java.text.SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(date)
                } else
                    return " • " + context.getString(R.string.mini_hours, hours.toInt())
            } else
                return " • " + context.getString(R.string.mini_minutes, minutes.toInt())
        } else
            return " • " + context.getString(R.string.mini_seconds, seconds.toInt())
    }

}
