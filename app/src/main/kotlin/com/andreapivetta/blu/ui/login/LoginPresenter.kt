package com.andreapivetta.blu.ui.login

import com.andreapivetta.blu.common.pref.AppSettings
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 15/05/16.
 */
class LoginPresenter(val settings: AppSettings) : BasePresenter<LoginMvpView>() {

    companion object {
        val CODE_OAUTH = 0
    }

    override fun attachView(mvpView: LoginMvpView) {
        super.attachView(mvpView)
        if (settings.isUserLoggedIn())
            mvpView.moveOn()
    }

    fun performLogin() {
        mvpView?.showOauthActivity(CODE_OAUTH)
    }

    fun onResultOk() {
        mvpView?.moveOn()
    }

    fun onResultCanceled() {
        mvpView?.showLoginCanceled()
    }

}