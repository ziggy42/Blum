package com.andreapivetta.blu.ui.profile

import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettings
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class UserPresenter(val settings: AppSettings) : BasePresenter<UserMvpView>() {

    private val disposables = CompositeDisposable()

    private var page = 1

    private lateinit var user: User

    var relationshipDataAvailable: Boolean = false
    var isLoggedUserFollowing: Boolean = false
    var isLoggedUser: Boolean = false

    override fun detachView() {
        super.detachView()
        disposables.clear()
    }

    fun loadUser(screenName: String) {
        checkViewAttached()
        mvpView?.showLoading()

        disposables.add(TwitterAPI.showUser(screenName)
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
                }))
    }

    fun loadUser(id: Long) {
        checkViewAttached()
        mvpView?.showLoading()

        disposables.add(TwitterAPI.showUser(id)
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
                }))
    }

    fun loadUserData(user: User) {
        this.user = user
        checkViewAttached()
        loadFriendShip()
        loadUserTimeline()
    }

    fun onRefresh() {
        checkViewAttached()

        val sinceId = mvpView?.getLastTweetId()
        if (sinceId != null && sinceId > 0) {
            val page = Paging(1, 200)
            page.sinceId = sinceId
            disposables.add(TwitterAPI.refreshUserTimeLine(user.id, page)
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
                    }))
        } else mvpView?.stopRefresh()
    }

    private fun loadUserTimeline() {
        disposables.add(TwitterAPI.getUserTimeline(user.id, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.showTweets(it.map(::Tweet).toMutableList())
                }, { Timber.e(it?.message) }))
    }

    private fun loadFriendShip() {
        if (settings.getLoggedUserId() == user.id) {
            isLoggedUser = true
            mvpView?.showUpdateFriendshipControls()
        } else {
            disposables.add(TwitterAPI.getFriendship(settings.getLoggedUserId(), user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        relationshipDataAvailable = true
                        isLoggedUserFollowing = it.isSourceFollowingTarget
                        mvpView?.showUpdateFriendshipControls()
                    }, { Timber.e(it) }))
        }
    }

    fun updateFriendship() {
        val single: Single<User> = if (isLoggedUserFollowing) TwitterAPI.unfollow(user.id) else
            TwitterAPI.follow(user.id)
        disposables.add(single
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isLoggedUserFollowing = !isLoggedUserFollowing
                    mvpView?.updateFriendshipStatus(isLoggedUserFollowing)
                }, { Timber.e(it) }))
    }

    fun favorite(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.favorite(tweet.id)
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
                }))
    }

    fun retweet(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.retweet(tweet.id)
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
                }))
    }

    fun unfavorite(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.unfavorite(tweet.id)
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
                }))
    }

    fun unretweet(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.unretweet(tweet.status.currentUserRetweetId)
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
                }))
    }

}