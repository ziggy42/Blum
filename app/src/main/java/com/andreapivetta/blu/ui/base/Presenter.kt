package com.andreapivetta.blu.ui.base

/**
 * Created by andrea on 15/05/16.
 */
interface Presenter<in V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()

}