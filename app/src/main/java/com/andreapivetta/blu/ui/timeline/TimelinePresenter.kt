package com.andreapivetta.blu.ui.timeline

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.twitter.TwitterAPI
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.Status

/**
 * Created by andrea on 17/05/16.
 */
class TimelinePresenter : BasePresenter<TimelineMvpView>() {

    private var page: Int = 1
    private var mSubscriber: Subscription? = null
    private var mRefreshSubscriber: Subscription? = null

    override fun detachView() {
        super.detachView()
        if (mSubscriber != null)
            mSubscriber?.unsubscribe()
        if (mRefreshSubscriber != null)
            mRefreshSubscriber?.unsubscribe()
    }

    fun getTweets() {
        checkViewAttached()
        mvpView?.showLoading()

        mSubscriber = TwitterAPI.getHomeTimeline(Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<MutableList<Status>>() {
                    override fun onSuccess(list: MutableList<Status>?) {
                        mvpView?.hideLoading()
                        if (list!!.isEmpty())
                            mvpView?.showEmpty()
                        else
                            mvpView?.showTweets(list)
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.hideLoading()
                        mvpView?.showError()
                    }
                })
    }

    fun onRefresh() {
        checkViewAttached()

        val page = Paging(1, 200)
        page.sinceId = mvpView!!.getLastTweetId()

        mRefreshSubscriber = TwitterAPI.refreshTimeLine(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<MutableList<Status>>() {
                    override fun onSuccess(list: MutableList<Status>?) {
                        mvpView?.stopRefresh()
                        list?.reversed()?.forEach { status -> mvpView?.showTweet(status) }
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.stopRefresh()
                    }
                })
    }

}