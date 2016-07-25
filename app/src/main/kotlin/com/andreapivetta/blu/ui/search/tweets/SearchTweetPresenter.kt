package com.andreapivetta.blu.ui.search.tweets

import com.andreapivetta.blu.data.TwitterAPI
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.TimelinePresenter
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Query
import twitter4j.QueryResult

/**
 * Created by andrea on 25/07/16.
 */
class SearchTweetPresenter(textQuery: String) : TimelinePresenter() {

    private var query: Query? = Query(textQuery)
    private var queryResult: QueryResult? = null

    override fun getTweets() {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        mSubscriber = TwitterAPI.searchTweets(query!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<QueryResult>() {
                    override fun onSuccess(result: QueryResult?) {
                        mvpView?.hideLoading()

                        queryResult = result
                        val list = result?.tweets
                        when {
                            list == null -> mvpView?.showError()
                            list.isEmpty() -> mvpView?.showEmpty()
                            else -> {
                                mvpView?.showTweets(list.map { status -> Tweet(status) }
                                        .toMutableList())
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
        if (queryResult != null && queryResult!!.hasNext()) {
            query = queryResult?.nextQuery()
            mRefreshSubscriber = TwitterAPI.searchTweets(query!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : SingleSubscriber<QueryResult>() {
                        override fun onSuccess(result: QueryResult?) {
                            queryResult = result
                            val list = result?.tweets
                            if (list != null)
                                mvpView?.showMoreTweets(list
                                        .map { status -> Tweet(status) }.toMutableList())
                        }

                        override fun onError(error: Throwable?) {
                            Timber.e(error?.message)
                        }
                    })
        }
    }

    override fun onRefresh() {
        // TODO
        mvpView?.stopRefresh()
    }

}