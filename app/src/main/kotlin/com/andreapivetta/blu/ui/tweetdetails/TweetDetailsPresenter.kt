package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.twitter.TwitterAPI
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Status

/**
 * Created by andrea on 25/05/16.
 */
class TweetDetailsPresenter : BasePresenter<TweetDetailsMvpView>() {

    private var mSubscriber: Subscription? = null
    private var isLoading: Boolean = false

    override fun detachView() {
        super.detachView()
        mSubscriber?.unsubscribe()
    }

    fun getConversation(statusId: Long) {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        mSubscriber = TwitterAPI.getConversation(statusId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Pair<MutableList<Status>, Int>>() {
                    override fun onSuccess(value: Pair<MutableList<Status>, Int>?) {
                        mvpView?.hideLoading()
                        val (list, index) = value!!
                        mvpView?.setHeaderIndex(index)

                        if (list.isEmpty())
                            mvpView?.showEmpty()
                        else
                            mvpView?.showTweets(list)
                        isLoading = false
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }
                })
    }

}