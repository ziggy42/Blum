package com.andreapivetta.blu.ui.timeline

import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.User

/**
 * Created by andrea on 17/05/16.
 */
open class TimelinePresenter : BasePresenter<TimelineMvpView>() {

    var page: Int = 1
    protected var isLoading: Boolean = false
    protected val disposables = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        disposables.clear()
    }

    open fun getTweets() {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        disposables.add(TwitterAPI.getHomeTimeline(Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.hideLoading()

                    when {
                        it == null -> mvpView?.showError()
                        it.isEmpty() -> mvpView?.showEmpty()
                        else -> {
                            mvpView?.showTweets(it.map(::Tweet).toMutableList())
                            page++
                        }
                    }

                    isLoading = false
                }, {
                    Timber.e(it?.message)
                    mvpView?.hideLoading()
                    mvpView?.showError()
                    isLoading = false
                }))
    }

    open fun getMoreTweets() {
        if (isLoading)
            return

        checkViewAttached()
        isLoading = true

        disposables.add(TwitterAPI.getHomeTimeline(Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        if (it.isNotEmpty())
                            mvpView?.showMoreTweets(it.map(::Tweet).toMutableList())
                        page++
                    }
                    isLoading = false
                }, {
                    Timber.e(it?.message)
                    isLoading = false
                }))
    }

    open fun onRefresh() {
        checkViewAttached()

        val sinceId = mvpView?.getLastTweetId()
        if (sinceId != null && sinceId > 0) {
            val page = Paging(1, 200)
            page.sinceId = sinceId
            disposables.add(TwitterAPI.refreshTimeLine(page)
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

    fun reply(tweet: Tweet, user: User) {
        mvpView?.showNewTweet(tweet, user)
    }

}