package com.andreapivetta.blu.ui.mediatimeline

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.TwitterAPI
import com.andreapivetta.blu.ui.mediatimeline.model.Media
import rx.Observable
import rx.SingleSubscriber
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.Status

/**
 * Created by andrea on 24/06/16.
 */
class MediaPresenter(val userId: Long) : BasePresenter<MediaMvpView>() {

    var page: Int = 1
    private var isLoading: Boolean = false
    private var mSubscriber: Subscription? = null
    private var mFavoriteSubscriber: Subscription? = null
    private var mUnfavoriteSubscriber: Subscription? = null
    private var mRetweetSubscriber: Subscription? = null

    override fun detachView() {
        super.detachView()
        mSubscriber?.unsubscribe()
        mFavoriteSubscriber?.unsubscribe()
        mUnfavoriteSubscriber?.unsubscribe()
        mRetweetSubscriber?.unsubscribe()
    }

    fun getPhotos() {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        mSubscriber = TwitterAPI.getUserTimeline(userId, Paging(page, 200))
                .flatMap { list -> Observable.from(list) }
                .filter { status -> status.mediaEntities.size > 0 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Status>() {
                    override fun onNext(status: Status) {
                        mvpView?.hideLoading()
                        mvpView?.showPhoto(Media(status))
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e, "Error!")
                    }

                    override fun onCompleted() {
                        page++
                        isLoading = false
                    }
                })
    }

    fun getMorePhotos() {
        if (isLoading)
            return

        checkViewAttached()
        isLoading = true

        TwitterAPI.getUserTimeline(userId, Paging(page, 200))
                .flatMap { list -> Observable.from(list) }
                .filter { status -> status.mediaEntities.size > 0 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Status>() {
                    override fun onNext(status: Status) {
                        mvpView?.hideLoading()
                        mvpView?.showPhoto(Media(status))
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e, "Error!")
                    }

                    override fun onCompleted() {
                        page++
                        isLoading = false
                    }
                })
    }

    fun favorite(media: Media) {
        checkViewAttached()

        mFavoriteSubscriber = TwitterAPI.favorite(media.tweetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Status?) {
                        media.favorite = true
                        mvpView?.onNewInteraction()
                    }
                })
    }

    fun retweet(media: Media) {
        checkViewAttached()

        mRetweetSubscriber = TwitterAPI.retweet(media.tweetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Status?) {
                        media.retweet = true
                        mvpView?.onNewInteraction()
                    }
                })
    }

    fun unfavorite(media: Media) {
        checkViewAttached()

        mUnfavoriteSubscriber = TwitterAPI.unfavorite(media.tweetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                    }

                    override fun onSuccess(value: Status?) {
                        media.favorite = false
                        mvpView?.onNewInteraction()
                    }
                })
    }
}