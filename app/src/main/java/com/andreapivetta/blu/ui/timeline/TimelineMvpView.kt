package com.andreapivetta.blu.ui.timeline

import com.andreapivetta.blu.arch.MvpView
import twitter4j.Status

/**
 * Created by andrea on 17/05/16.
 */
interface TimelineMvpView : MvpView {

    fun showTweets(tweets: MutableList<Status>)

    fun showEmpty()

    fun showError()

    fun showSnackBar(message: String)

    fun showLoading()

    fun hideLoading()

}