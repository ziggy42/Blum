package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.User

/**
 * Created by andrea on 25/05/16.
 */
class TweetDetailsPresenter : BasePresenter<TweetDetailsMvpView>() {

    private var isLoading: Boolean = false
    private var subscription: Subscription? = null
    private var retweetSubscription: Subscription? = null
    private var favoriteSubscription: Subscription? = null
    private var unfavoriteSubscription: Subscription? = null
    private var unretweetSubscription: Subscription? = null

    override fun detachView() {
        super.detachView()
        subscription?.unsubscribe()
        favoriteSubscription?.unsubscribe()
        unfavoriteSubscription?.unsubscribe()
        unretweetSubscription?.unsubscribe()
    }

    fun getConversation(statusId: Long) {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        subscription = TwitterAPI.getConversation(statusId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.hideLoading()

                    if (it == null)
                        mvpView?.showError()
                    else {
                        val (list, index) = it
                        if (list.isEmpty())
                            mvpView?.showError()
                        else
                            mvpView?.showTweets(index, list.map(::Tweet).toMutableList())
                    }
                    isLoading = false
                }, { Timber.e(it) })
    }

    fun favorite(tweet: Tweet) {
        checkViewAttached()

        favoriteSubscription = TwitterAPI.favorite(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.favorited = true
                    tweet.favoriteCount++
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) })
    }

    fun retweet(tweet: Tweet) {
        checkViewAttached()

        retweetSubscription = TwitterAPI.retweet(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.retweeted = true
                    tweet.retweetCount++
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) })
    }

    fun unfavorite(tweet: Tweet) {
        checkViewAttached()

        unfavoriteSubscription = TwitterAPI.unfavorite(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.favorited = false
                    tweet.favoriteCount--
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) })
    }

    fun unretweet(tweet: Tweet) {
        checkViewAttached()

        unfavoriteSubscription = TwitterAPI.unretweet(tweet.status.currentUserRetweetId)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        tweet.retweeted = false
                        tweet.retweetCount--
                        mvpView?.updateRecyclerViewView()
                    } else {
                        mvpView?.showSnackBar(R.string.error_unretweet)
                    }
                }, {
                    Timber.e(it)
                    mvpView?.showSnackBar(R.string.error_unretweet)
                })
    }

    fun reply(tweet: Tweet, user: User) {
        mvpView?.showNewTweet(tweet, user)
    }

}