package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.base.MvpView
import twitter4j.User

/**
 * Created by andrea on 24/05/16.
 */
interface TweetDetailsMvpView : MvpView {

    fun showTweets(headerIndex: Int, tweets: MutableList<Tweet>)

    fun showError()

    fun showSnackBar(stringResource: Int)

    fun showLoading()

    fun hideLoading()

    fun showNewTweet(tweet: Tweet, user: User)

    fun updateRecyclerViewView()

}