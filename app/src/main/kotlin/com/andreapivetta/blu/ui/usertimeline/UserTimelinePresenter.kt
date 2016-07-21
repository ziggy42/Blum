package com.andreapivetta.blu.ui.usertimeline

import com.andreapivetta.blu.data.TwitterAPI
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.TimelinePresenter
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.Status

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
                .subscribe(object : Subscriber<MutableList<Status>>() {

                    override fun onCompleted() {

                    }

                    override fun onNext(list: MutableList<Status>?) {
                        mvpView?.hideLoading()

                        when {
                            list == null -> mvpView?.showError()
                            list.isEmpty() -> mvpView?.showEmpty()
                            else -> {
                                mvpView?.showTweets(list.map { status -> Tweet(status) }.toMutableList())
                                page++
                            }
                        }

                        isLoading = false
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.hideLoading()
                        mvpView?.showError()
                        isLoading = false
                    }
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
                .subscribe(object : Subscriber<MutableList<Status>>() {

                    override fun onCompleted() {

                    }

                    override fun onNext(list: MutableList<Status>?) {
                        if (list != null) {
                            if (list.isNotEmpty())
                                mvpView?.showMoreTweets(list.map { status -> Tweet(status) }.toMutableList())
                            page++
                        }
                        isLoading = false
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        isLoading = false
                    }
                })
    }

    override fun onRefresh() {
        checkViewAttached()

        val page = Paging(1, 200)
        page.sinceId = mvpView!!.getLastTweetId()

        mRefreshSubscriber = TwitterAPI.refreshUserTimeLine(userId, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<MutableList<Status>>() {

                    override fun onCompleted() {

                    }

                    override fun onNext(list: MutableList<Status>?) {
                        mvpView?.stopRefresh()
                        list?.reversed()?.forEach { status -> mvpView?.showTweet(Tweet(status)) }
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.stopRefresh()
                    }
                })
    }

}