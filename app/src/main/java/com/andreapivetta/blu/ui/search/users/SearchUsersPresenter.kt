package com.andreapivetta.blu.ui.search.users

import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging

/**
 * Created by andrea on 25/07/16.
 */
class SearchUsersPresenter(private val textQuery: String) : BasePresenter<SearchUsersMvpView>() {

    var page: Int = 1
    private var isLoading: Boolean = false
    private var mSubscriber: Subscription? = null

    fun getUsers() {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        mSubscriber = TwitterAPI.searchUsers(textQuery, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.hideLoading()

                    when {
                        it == null -> mvpView?.showError()
                        it.isEmpty() -> mvpView?.showEmpty()
                        else -> {
                            mvpView?.showUsers(it)
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

    fun getMoreUsers() {
        if (isLoading)
            return

        checkViewAttached()
        isLoading = true

        mSubscriber = TwitterAPI.searchUsers(textQuery, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        if (it.isNotEmpty())
                            mvpView?.showMoreUsers(it)
                        page++
                    }
                    isLoading = false
                }, {
                    Timber.e(it)
                    isLoading = false
                })
    }

}