package com.andreapivetta.blu.ui.main

import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 18/05/16.
 */
class MainPresenter : BasePresenter<MainMvpView>() {

    fun fabClicked() {
        mvpView?.newTweet()
    }

}