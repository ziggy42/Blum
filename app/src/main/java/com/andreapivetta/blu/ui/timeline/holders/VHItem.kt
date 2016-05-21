package com.andreapivetta.blu.ui.timeline.holders


import android.content.Context
import android.support.annotation.CallSuper
import android.support.v7.app.AlertDialog
import android.text.util.Linkify
import android.view.View
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide
import twitter4j.Status
import twitter4j.Twitter
import java.util.*
import java.util.concurrent.TimeUnit

open class VHItem(val container: View) : BaseViewHolder(container) {

    @CallSuper
    override fun setup(status: Status, context: Context, favorites: MutableList<Long>,
                       retweets: MutableList<Long>, twitter: Twitter) {
        val currentStatus: Status

        if (status.isRetweet) {
            currentStatus = status.retweetedStatus
            retweetTextView.visibility = View.VISIBLE
            retweetTextView.text = context.getString(R.string.retweeted_by, status.user.screenName)
        } else {
            currentStatus = status
            retweetTextView.visibility = View.GONE
        }

        val currentUser = currentStatus.user
        userNameTextView.text = currentUser.name
        userScreenNameTextView.text = "@" + currentUser.screenName

        val c = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c2.time = currentStatus.createdAt

        val diff = c.timeInMillis - c2.timeInMillis
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds > 60) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            if (minutes > 60) {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours > 24) {
                    if (c.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                        timeTextView.text = " • " + java.text.SimpleDateFormat("MMM dd", Locale.getDefault()).format(currentStatus.createdAt)
                    else
                        timeTextView.text = " • " + java.text.SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(currentStatus.createdAt)
                } else
                    timeTextView.text = " • " + context.getString(R.string.mini_hours, hours.toInt())
            } else
                timeTextView.text = " • " + context.getString(R.string.mini_minutes, minutes.toInt())
        } else
            timeTextView.text = " • " + context.getString(R.string.mini_seconds, seconds.toInt())

        Glide.with(context).load(currentUser.biggerProfileImageURL)
                .dontAnimate()
                .into(userProfilePicImageView)

        if (currentStatus.isFavorited || favorites.contains(currentStatus.id))
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red_a700_36dp)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_grey_600_36dp)

        if (currentStatus.isRetweeted || retweets.contains(currentStatus.id))
            retweetImageButton.setImageResource(R.drawable.ic_repeat_green_a700_36dp)
        else
            retweetImageButton.setImageResource(R.drawable.ic_repeat_grey600_36dp)

        favouritesStatsTextView.text = status.favoriteCount.toString()
        retweetsStatsTextView.text = status.retweetCount.toString()

        val entities = status.extendedMediaEntities
        var text = currentStatus.text
        for (i in entities.indices)
            text = text.replace(entities[i].url, "")
        statusTextView.text = text
        Linkify.addLinks(statusTextView, Linkify.ALL)

        userProfilePicImageView.setOnClickListener { /* TODO */ }

        favouriteImageButton.setOnClickListener { /* TODO */ }

        retweetImageButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.retweet_title)).setPositiveButton(R.string.retweet) { dialog, which -> /* TODO */ }
                    .setNegativeButton(R.string.cancel, null).create().show()
        }

        respondImageButton.setOnClickListener { /* TODO */ }

        container.setOnClickListener { /* TODO */ }
    }
}
