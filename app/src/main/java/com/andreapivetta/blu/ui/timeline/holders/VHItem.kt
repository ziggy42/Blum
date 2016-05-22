package com.andreapivetta.blu.ui.timeline.holders


import android.support.annotation.CallSuper
import android.text.util.Linkify
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.bumptech.glide.Glide
import twitter4j.Status

open class VHItem(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        BaseViewHolder(container, listener, tweetInfoProvider) {

    @CallSuper
    override fun setup(status: Status) {
        val currentStatus: Status

        if (status.isRetweet) {
            currentStatus = status.retweetedStatus
            retweetTextView.visibility = View.VISIBLE
            retweetTextView.text = container.context.getString(R.string.retweeted_by, status.user.screenName)
        } else {
            currentStatus = status
            retweetTextView.visibility = View.GONE
        }

        val currentUser = currentStatus.user
        userNameTextView.text = currentUser.name
        userScreenNameTextView.text = "@" + currentUser.screenName
        timeTextView.text = formatDate(currentStatus.createdAt, container.context)

        Glide.with(container.context).load(currentUser.biggerProfileImageURL)
                .dontAnimate()
                .into(userProfilePicImageView)

        if (currentStatus.isFavorited || tweetInfoProvider.isFavorite(currentStatus))
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red_a700_36dp)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_grey_600_36dp)

        if (currentStatus.isRetweeted || tweetInfoProvider.isRetweet(currentStatus))
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

        userProfilePicImageView.setOnClickListener { listener.showUser(currentUser) }

        favouriteImageButton.setOnClickListener {
            if (currentStatus.isFavorited || tweetInfoProvider.isFavorite(currentStatus)) {
                listener.unfavorite(currentStatus)
                favouriteImageButton.setImageResource(R.drawable.ic_favorite_grey_600_36dp)
            } else {
                listener.favorite(currentStatus)
                favouriteImageButton.setImageResource(R.drawable.ic_favorite_red_a700_36dp)
            }
        }

        retweetImageButton.setOnClickListener {
            if (currentStatus.isRetweeted || tweetInfoProvider.isRetweet(currentStatus)) {
                listener.unretweet(currentStatus)
                retweetImageButton.setImageResource(R.drawable.ic_repeat_grey600_36dp)
            } else {
                listener.retweet(currentStatus)
                favouriteImageButton.setImageResource(R.drawable.ic_repeat_green_a700_36dp)
            }
        }

        respondImageButton.setOnClickListener { listener.replay(currentStatus) }

        container.setOnClickListener { listener.showTweet(currentStatus) }
    }
}
