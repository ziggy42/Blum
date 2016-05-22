package com.andreapivetta.blu.ui.main

import com.andreapivetta.blu.arch.MvpView

/**
 * Created by andrea on 17/05/16.
 */
interface MainMvpView : MvpView {

    fun openSettings()

    fun openNotifications()

    fun openDirectMessages()

    fun newTweet()

}