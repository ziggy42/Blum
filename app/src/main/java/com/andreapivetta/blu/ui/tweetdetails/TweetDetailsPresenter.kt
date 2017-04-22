package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import twitter4j.User

/**
 * Created by andrea on 25/05/16.
 */
class TweetDetailsPresenter : BasePresenter<TweetDetailsMvpView>() {

    private var isLoading = false
    private val disposables = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        disposables.clear()
    }

    fun getConversation(statusId: Long) {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        disposables.add(TwitterAPI.getConversation(statusId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.hideLoading()

                    if (it == null)
                        mvpView?.showError()
                    else {
                        val (list, index) = it
                        if (list.isEmpty())
                            mvpView?.showError()
                        else
                            mvpView?.showTweets(index, list.map(::Tweet).toMutableList())
                    }
                    isLoading = false
                }, { Timber.e(it) }))
    }

    fun favorite(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.favorite(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.favorited = true
                    tweet.favoriteCount++
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) }))
    }

    fun retweet(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.retweet(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.retweeted = true
                    tweet.retweetCount++
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) }))
    }

    fun unfavorite(tweet: Tweet) {
        checkViewAttached()

        disposables.add(TwitterAPI.unfavorite(tweet.id)
                .map(::Tweet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tweet.favorited = false
                    tweet.favoriteCount--
                    mvpView?.updateRecyclerViewView()
                }, { Timber.e(it) }))
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
                    Timber.e(it)
                    mvpView?.showSnackBar(R.string.error_unretweet)
                }))
    }

    fun reply(tweet: Tweet, user: User) {
        mvpView?.showNewTweet(tweet, user)
    }

}