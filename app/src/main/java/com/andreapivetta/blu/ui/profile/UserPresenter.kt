package com.andreapivetta.blu.ui.profile

import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class UserPresenter : BasePresenter<UserMvpView>() {

    private var page = 1
    private var subscription: Subscription? = null
    private var unfavoriteSubscription: Subscription? = null
    private var unretweetSubscription: Subscription? = null
    private var retweetSubscription: Subscription? = null
    private var favoriteSubscription: Subscription? = null
    private var refreshSubscription: Subscription? = null

    private var user: User? = null

    override fun detachView() {
        super.detachView()
        subscription?.unsubscribe()
    }

    fun loadUser(screenName: String) {
        checkViewAttached()
        mvpView?.showLoading()

        subscription = TwitterAPI.showUser(screenName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    user = it
                    mvpView?.hideLoading()
                    mvpView?.setupUser(it)
                    loadUserData(it)
                }, {
                    Timber.e(it?.message)
                    mvpView?.hideLoading()
                    mvpView?.showError()
                })
    }

    fun loadUserData(user: User) {
        this.user = user
        checkViewAttached()

        subscription = TwitterAPI.getUserTimeline(user.id, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.showTweets(it.map(::Tweet).toMutableList())
                }, { Timber.e(it?.message) })
    }

    fun onRefresh() {
        checkViewAttached()

        val page = Paging(1, 200)
        page.sinceId = mvpView!!.getLastTweetId()

        refreshSubscription = TwitterAPI.refreshUserTimeLine(user!!.id, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.stopRefresh()
                    if (it != null) {
                        it.reversed().forEach { status -> mvpView?.showTweet(Tweet(status)) }
                    } else {
                        mvpView?.showSnackBar(R.string.error_refreshing_timeline)
                    }
                }, {
                    Timber.e(it?.message)
                    mvpView?.stopRefresh()
                    mvpView?.showSnackBar(R.string.error_refreshing_timeline)
                })
    }

    fun favorite(tweet: Tweet) {
        checkViewAttached()

        favoriteSubscription = TwitterAPI.favorite(tweet.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(::Tweet)
                .subscribe({
                    if (it != null) {
                        tweet.favorited = true
                        tweet.favoriteCount++
                        mvpView?.updateRecyclerViewView()
                    } else {
                        mvpView?.showSnackBar(R.string.error_favorite)
                    }
                }, {
                    Timber.e(it?.message)
                    mvpView?.showSnackBar(R.string.error_favorite)
                })
    }

    fun retweet(tweet: Tweet) {
        checkViewAttached()

        retweetSubscription = TwitterAPI.retweet(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        tweet.retweeted = true
                        tweet.retweetCount++
                        mvpView?.updateRecyclerViewView()
                    } else {
                        mvpView?.showSnackBar(R.string.error_retweet)
                    }
                }, {
                    Timber.e(it?.message)
                    mvpView?.showSnackBar(R.string.error_retweet)
                })
    }

    fun unfavorite(tweet: Tweet) {
        checkViewAttached()

        unfavoriteSubscription = TwitterAPI.unfavorite(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        tweet.favorited = false
                        tweet.favoriteCount--
                        mvpView?.updateRecyclerViewView()
                    } else {
                        mvpView?.showSnackBar(R.string.error_unfavorite)
                    }
                }, {
                    Timber.e(it?.message)
                    mvpView?.showSnackBar(R.string.error_unfavorite)
                })
    }

    fun unretweet(tweet: Tweet) {
        checkViewAttached()

        unretweetSubscription = TwitterAPI.unretweet(tweet.status.currentUserRetweetId)
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
                    Timber.e(it?.message)
                    mvpView?.showSnackBar(R.string.error_unretweet)
                })
    }

}