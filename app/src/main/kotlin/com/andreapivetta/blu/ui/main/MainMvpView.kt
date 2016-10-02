package com.andreapivetta.blu.ui.main

import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 17/05/16.
 */
interface MainMvpView : MvpView {

    fun newTweet()

    fun search(string: String)

    fun openSettings()

    fun openNotifications()

    fun openMessages()

    fun openProfile()

}