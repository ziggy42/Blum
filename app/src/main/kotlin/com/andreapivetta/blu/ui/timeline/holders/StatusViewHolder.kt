package com.andreapivetta.blu.ui.timeline.holders


import android.support.annotation.CallSuper
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide

open class StatusViewHolder(container: View, listener: InteractionListener) :
        BaseViewHolder(container, listener) {

    protected var retweetTextView: TextView

    init {
        this.retweetTextView = container.findViewById(R.id.retweetTextView) as TextView
    }

    @CallSuper
    override fun setup(tweet: Tweet) {
        val currentTweet: Tweet

        if (tweet.retweet) {
            currentTweet = tweet.getRetweetedTweet()
            retweetTextView.visibility = View.VISIBLE
            retweetTextView.text = container.context.getString(
                    R.string.retweeted_by, tweet.user.screenName)
        } else {
            currentTweet = tweet
            retweetTextView.visibility = View.GONE
        }

        val currentUser = currentTweet.user
        userNameTextView.text = currentUser.name
        userScreenNameTextView.text = "@${currentUser.screenName}"
        timeTextView.text = " • ${formatDate(currentTweet.timeStamp, container.context)}"

        Glide.with(container.context).load(currentUser.biggerProfileImageURL)
                .dontAnimate()
                .into(userProfilePicImageView)

        if (currentTweet.favorited)
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite)

        if (currentTweet.retweeted)
            retweetImageButton.setImageResource(R.drawable.ic_repeat_green)
        else
            retweetImageButton.setImageResource(R.drawable.ic_repeat)

        favouritesStatsTextView.text = "${tweet.favoriteCount}"
        retweetsStatsTextView.text = "${tweet.retweetCount}"

        statusTextView.text = currentTweet.getTextWithoutMediaURLs()
        Linkify.addLinks(statusTextView, Linkify.ALL)

        userProfilePicImageView.setOnClickListener { listener.showUser(currentUser) }

        favouriteImageButton.setOnClickListener {
            if (currentTweet.favorited)
                listener.unfavorite(currentTweet)
            else
                listener.favorite(currentTweet)
        }

        retweetImageButton.setOnClickListener {
            if (currentTweet.retweeted)
                listener.unretweet(currentTweet)
            else
                listener.retweet(currentTweet)
        }

        respondImageButton.setOnClickListener { listener.reply(currentTweet, currentUser) }
        container.setOnClickListener { listener.openTweet(currentTweet, currentUser) }
    }
}
