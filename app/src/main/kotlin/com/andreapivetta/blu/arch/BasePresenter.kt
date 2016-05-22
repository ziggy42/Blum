package com.andreapivetta.blu.arch

import com.andreapivetta.blu.arch.exception.MvpViewNotAttachedException

/**
 * Created by andrea on 15/05/16.
 */
open class BasePresenter<V : MvpView> : Presenter<V> {

    protected  var mvpView: V? = null

    override fun attachView(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        this.mvpView = null
    }

    fun isViewAttached() = mvpView != null

    fun checkViewAttached() {
        if (!isViewAttached())
            throw MvpViewNotAttachedException()
    }
}