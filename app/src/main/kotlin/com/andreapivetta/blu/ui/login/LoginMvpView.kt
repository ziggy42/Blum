package com.andreapivetta.blu.ui.login

import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 15/05/16.
 */
interface LoginMvpView : MvpView {

    fun showOauthActivity(requestCode: Int)

    fun showLoginError()

    fun showLoginCanceled()

    fun moveOn()

}