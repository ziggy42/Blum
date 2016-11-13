package com.andreapivetta.blu.ui.search.tweets

import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.timeline.TimelinePresenter
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
                .subscribe({
                    mvpView?.hideLoading()

                    queryResult = it
                    val list = it?.tweets
                    when {
                        list == null -> mvpView?.showError()
                        list.isEmpty() -> mvpView?.showEmpty()
                        else -> {
                            mvpView?.showTweets(list.map(::Tweet).toMutableList())
                            page++
                        }
                    }

                    isLoading = false
                }, {
                    Timber.e(it?.message)
                    mvpView?.hideLoading()
                    mvpView?.showError()
                    isLoading = false
                })
    }

    override fun getMoreTweets() {
        if (queryResult != null && queryResult!!.hasNext()) {
            query = queryResult?.nextQuery()
            mRefreshSubscriber = TwitterAPI.searchTweets(query!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        queryResult = it
                        val list = it?.tweets
                        if (list != null)
                            mvpView?.showMoreTweets(list.map(::Tweet).toMutableList())
                    }, { Timber.e(it?.message) })
        }
    }

    override fun onRefresh() {
        // TODO
        mvpView?.stopRefresh()
    }

}