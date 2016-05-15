package com.andreapivetta.blu.arch

/**
 * Created by andrea on 15/05/16.
 */
interface Presenter<V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()

}