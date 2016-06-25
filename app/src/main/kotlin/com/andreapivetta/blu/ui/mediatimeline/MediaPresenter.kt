package com.andreapivetta.blu.ui.mediatimeline

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.twitter.TwitterAPI
import rx.Observable
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

    override fun detachView() {
        super.detachView()
        mSubscriber?.unsubscribe()
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
                        Timber.i(status.mediaEntities[0].mediaURL)
                        mvpView?.showPhoto(status.extendedMediaEntities[0].mediaURLHttps)
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e, "Errror!")
                    }

                    override fun onCompleted() {
                        Timber.i("onCompleted!!")
                    }
                })
    }
}