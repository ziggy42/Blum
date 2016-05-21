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
        timeTextView.text = formatDate(currentStatus.createdAt, context)

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
            AlertDialog.Builder(context).setTitle(context.getString(R.string.retweet_title)).
                    setPositiveButton(R.string.retweet) { dialog, which -> /* TODO */ }
                    .setNegativeButton(R.string.cancel, null)
                    .create().show()
        }

        respondImageButton.setOnClickListener { /* TODO */ }

        container.setOnClickListener { /* TODO */ }
    }
}
