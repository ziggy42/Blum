package com.andreapivetta.blu.ui.timeline

import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.base.MvpView
import twitter4j.User

/**
 * Created by andrea on 17/05/16.
 */
interface TimelineMvpView : MvpView {

    fun showTweets(tweets: MutableList<Tweet>)

    fun showTweet(tweet: Tweet)

    fun showMoreTweets(tweets: MutableList<Tweet>)

    fun getLastTweetId(): Long

    fun stopRefresh()

    fun showEmpty()

    fun showError()

    fun showSnackBar(stringResource: Int)

    fun showLoading()

    fun hideLoading()

    fun showNewTweet(tweet: Tweet, user: User)

    fun updateRecyclerViewView()

}