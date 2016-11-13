package com.andreapivetta.blu.ui.usertimeline

import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.timeline.TimelinePresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging

/**
 * Created by andrea on 25/06/16.
 */
class UserTimelinePresenter(val userId: Long) : TimelinePresenter() {

    override fun getTweets() {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        mSubscriber = TwitterAPI.getUserTimeline(userId, Paging(page, 50))
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
                    Timber.e(it)
                    mvpView?.hideLoading()
                    mvpView?.showError()
                    isLoading = false
                })
    }

    override fun getMoreTweets() {
        if (isLoading)
            return

        checkViewAttached()
        isLoading = true

        mSubscriber = TwitterAPI.getUserTimeline(userId, Paging(page, 50))
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
                    Timber.e(it)
                    isLoading = false
                })
    }

    override fun onRefresh() {
        checkViewAttached()

        val page = Paging(1, 200)
        page.sinceId = mvpView!!.getLastTweetId()

        mRefreshSubscriber = TwitterAPI.refreshUserTimeLine(userId, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.stopRefresh()
                    if (it != null)
                        it.reversed().forEach { status -> mvpView?.showTweet(Tweet(status)) }
                    else
                        mvpView?.showSnackBar(R.string.error_refreshing_timeline)
                }, {
                    Timber.e(it)
                    mvpView?.stopRefresh()
                    mvpView?.showSnackBar(R.string.error_refreshing_timeline)
                })
    }

}