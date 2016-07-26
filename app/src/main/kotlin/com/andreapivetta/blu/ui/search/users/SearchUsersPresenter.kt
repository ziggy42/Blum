package com.andreapivetta.blu.ui.search.users

import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Paging
import twitter4j.User

/**
 * Created by andrea on 25/07/16.
 */
class SearchUsersPresenter(val textQuery: String) : BasePresenter<SearchUsersMvpView>() {

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
                .subscribe(object : SingleSubscriber<MutableList<User>>() {
                    override fun onSuccess(list: MutableList<User>?) {
                        mvpView?.hideLoading()

                        when {
                            list == null -> mvpView?.showError()
                            list.isEmpty() -> mvpView?.showEmpty()
                            else -> {
                                mvpView?.showUsers(list)
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

    fun getMoreUsers() {
        if (isLoading)
            return

        checkViewAttached()
        isLoading = true

        mSubscriber = TwitterAPI.searchUsers(textQuery, Paging(page, 50))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<MutableList<User>>() {
                    override fun onSuccess(list: MutableList<User>?) {
                        if (list != null) {
                            if (list.isNotEmpty())
                                mvpView?.showMoreUsers(list)
                            page++
                        }
                        isLoading = false
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        isLoading = false
                        // don't show anything to the user
                    }
                })
    }

}