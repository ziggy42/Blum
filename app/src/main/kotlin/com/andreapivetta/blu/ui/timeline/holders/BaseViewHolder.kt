package com.andreapivetta.blu.ui.timeline.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseViewHolder(val container: View, val listener: InteractionListener) :
        RecyclerView.ViewHolder(container) {

    protected var userNameTextView = container.findViewById(R.id.userNameTextView) as TextView
    protected var userScreenNameTextView = container.findViewById(R.id.userScreenName_TextView) as TextView
    protected var statusTextView = container.findViewById(R.id.statusTextView) as TextView
    protected var timeTextView = container.findViewById(R.id.timeTextView) as TextView
    protected val retweetsStatsTextView = container.findViewById(R.id.retweetsStatsTextView) as TextView
    protected val favouritesStatsTextView = container.findViewById(R.id.favouritesStatsTextView) as TextView
    protected var userProfilePicImageView = container.findViewById(R.id.userProfilePicImageView) as ImageView
    protected var favouriteImageButton = container.findViewById(R.id.favouriteImageButton) as ImageButton
    protected var retweetImageButton = container.findViewById(R.id.retweetImageButton) as ImageButton
    protected var respondImageButton = container.findViewById(R.id.respondImageButton) as ImageButton

    abstract fun setup(tweet: Tweet)

    protected fun formatDate(timeStamp: Long, context: Context): String {
        val c = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c2.timeInMillis = timeStamp

        val diff = c.timeInMillis - timeStamp
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds > 60) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            if (minutes > 60) {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours > 24) {
                    if (c.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                        return java.text.SimpleDateFormat("MMM dd", Locale.getDefault()).format(c2.time)
                    else
                        return java.text.SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(c2.time)
                } else
                    return context.getString(R.string.mini_hours, hours.toInt())
            } else
                return context.getString(R.string.mini_minutes, minutes.toInt())
        } else
            return context.getString(R.string.mini_seconds, seconds.toInt())
    }

}
