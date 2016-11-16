package com.andreapivetta.blu.ui.profile

import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.base.MvpView
import com.andreapivetta.blu.ui.timeline.InteractionListener

/**
 * Created by andrea on 14/11/16.
 */
interface UserMvpView : MvpView, InteractionListener {

    fun follow()

    fun unfollow()

    fun showTweets(tweets: MutableList<Tweet>)

    fun showMoreTweets(tweets: MutableList<Tweet>)

    fun showSnackBar(stringResource: Int)

    fun updateRecyclerViewView()
}