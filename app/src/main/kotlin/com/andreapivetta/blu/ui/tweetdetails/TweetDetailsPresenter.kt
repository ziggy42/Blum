package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.data.twitter.model.Tweet
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Status
import twitter4j.User

/**
 * Created by andrea on 25/05/16.
 */
class TweetDetailsPresenter : BasePresenter<TweetDetailsMvpView>() {

    private var isLoading: Boolean = false
    private var mSubscriber: Subscription? = null
    private var mRetweetSubscriber: Subscription? = null
    private var mFavoriteSubscriber: Subscription? = null
    private var mUnfavoriteSubscriber: Subscription? = null
    private var mUnretweetSubscriber: Subscription? = null

    override fun detachView() {
        super.detachView()
        mSubscriber?.unsubscribe()
        mFavoriteSubscriber?.unsubscribe()
        mUnfavoriteSubscriber?.unsubscribe()
        mUnretweetSubscriber?.unsubscribe()
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

                        if (value == null)
                            mvpView?.showError()
                        else {
                            val (list, index) = value
                            mvpView?.setHeaderIndex(index)

                            if (list.isEmpty())
                                mvpView?.showEmpty()
                            else
                                mvpView?.showTweets(list.map { status -> Tweet(status) }.toMutableList())
                        }
                        isLoading = false
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }
                })
    }

    fun favorite(tweet: Tweet) {
        checkViewAttached()

        mFavoriteSubscriber = TwitterAPI.favorite(tweet.id)
                .map { status -> Tweet(status) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Tweet>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Tweet?) {
                        tweet.favorited = true
                        tweet.favoriteCount++
                        mvpView?.updateRecyclerViewView()
                    }
                })
    }

    fun retweet(tweet: Tweet) {
        checkViewAttached()

        mRetweetSubscriber = TwitterAPI.retweet(tweet.id)
                .map { status -> Tweet(status) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Tweet>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Tweet?) {
                        tweet.retweeted = true
                        tweet.retweetCount++
                        mvpView?.updateRecyclerViewView()
                    }
                })
    }

    fun unfavorite(tweet: Tweet) {
        checkViewAttached()

        mUnfavoriteSubscriber = TwitterAPI.unfavorite(tweet.id)
                .map { status -> Tweet(status) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Tweet>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Tweet?) {
                        tweet.favorited = false
                        tweet.favoriteCount--
                        mvpView?.updateRecyclerViewView()
                    }
                })
    }

    fun reply(tweet: Tweet, user: User) {
        mvpView?.showNewTweet(tweet, user)
    }

}