package com.andreapivetta.blu.ui.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.profile.viewholders.ProfileViewHolder
import com.andreapivetta.blu.ui.timeline.holders.*
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class UserAdapter(private val userMvpView: UserMvpView) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var user: User? = null
    var tweets: MutableList<Tweet> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? =
            when (viewType) {
                R.layout.tweet_basic -> StatusViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_basic, parent, false), userMvpView)
                R.layout.tweet_photo -> StatusPhotoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_photo, parent, false), userMvpView)
                R.layout.tweet_quote -> StatusQuoteViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_quote, parent, false), userMvpView)
                R.layout.tweet_multiplephotos -> StatusMultiplePhotosViewHolder(
                        LayoutInflater.from(parent?.context).inflate(
                                R.layout.tweet_multiplephotos, parent, false), userMvpView)
                R.layout.tweet_video -> StatusVideoViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_video, parent, false), userMvpView)
                R.layout.tweet_link -> StatusLinkViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.tweet_link, parent, false), userMvpView)
                R.layout.profile -> ProfileViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.profile, parent, false), userMvpView)
                else -> throw UnsupportedOperationException("No Type found")
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == 0)
            (holder as ProfileViewHolder).setup(user!!)
        else
            (holder as BaseViewHolder).setup(tweets[position - 1])
    }

    override fun getItemCount() = if (user != null) tweets.size + 1 else 0

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return R.layout.profile

        val tweet = tweets[position - 1]
        when {
            tweet.hasSingleImage() -> return R.layout.tweet_photo
            tweet.hasSingleVideo() -> return R.layout.tweet_video
            tweet.hasMultipleMedia() -> return R.layout.tweet_multiplephotos
            tweet.quotedStatus -> return R.layout.tweet_quote
            tweet.hasLinks() -> return R.layout.tweet_link
            else -> return R.layout.tweet_basic
        }
    }
}