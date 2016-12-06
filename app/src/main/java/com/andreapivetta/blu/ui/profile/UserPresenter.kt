package com.andreapivetta.blu.ui.profile

import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettings
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class UserPresenter(val settings: AppSettings) : BasePresenter<UserMvpView>() {

    private var page = 1
    private var userTimelineSubscription: Subscription? = null
    private var relationshipSubscription: Subscription? = null
    private var unfavoriteSubscription: Subscription? = null
    private var unretweetSubscription: Subscription? = null
    private var retweetSubscription: Subscription? = null
    private var favoriteSubscription: Subscription? = null
    private var refreshSubscription: Subscription? = null
    private var updateFriendshipSubscription: Subscription? = null

    private lateinit var user: User

    var relationshipDataAvailable: Boolean = false
    var isLoggedUserFollowing: Boolean = false
    var isLoggedUser: Boolean = false

    override fun detachView() {
        super.detachView()
        userTimelineSubscription?.unsubscribe()
        relationshipSubscription?.unsubscribe()
    }

    fun loadUser(screenName: String) {
        checkViewAttached()
        mvpView?.showLoading()

        userTimelineSubscription = TwitterAPI.showUser(screenName)
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

    fun loadUser(id: Long) {
        checkViewAttached()
        mvpView?.showLoading()

        userTimelineSubscription = TwitterAPI.showUser(id)
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
        loadFriendShip()
        loadUserTimeline()
    }

    fun onRefresh() {
        checkViewAttached()

        val page = Paging(1, 200)
        page.sinceId = mvpView!!.getLastTweetId()

        refreshSubscription = TwitterAPI.refreshUserTimeLine(user.id, page)
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

    private fun loadUserTimeline() {
        userTimelineSubscription = TwitterAPI.getUserTimeline(user.id, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.showTweets(it.map(::Tweet).toMutableList())
                }, { Timber.e(it?.message) })
    }

    private fun loadFriendShip() {
        if (settings.getLoggedUserId() == user.id) {
            isLoggedUser = true
            mvpView?.showUpdateFriendshipControls()
        } else {
            relationshipSubscription = TwitterAPI.getFriendship(settings.getLoggedUserId(), user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        relationshipDataAvailable = true
                        isLoggedUserFollowing = it.isSourceFollowingTarget
                        mvpView?.showUpdateFriendshipControls()
                    }, { Timber.e(it) })
        }
    }

    fun updateFriendship() {
        val single: Single<User> = if (isLoggedUserFollowing) TwitterAPI.unfollow(user.id) else
            TwitterAPI.follow(user.id)
        updateFriendshipSubscription = single.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isLoggedUserFollowing = !isLoggedUserFollowing
                    mvpView?.updateFriendshipStatus(isLoggedUserFollowing)
                }, { Timber.e(it) })
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