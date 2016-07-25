package com.andreapivetta.blu.ui.main

import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 17/05/16.
 */
interface MainMvpView : MvpView {

    fun openSettings()

    fun openNotifications()

    fun openDirectMessages()

    fun search(string: String)

    fun newTweet()

}