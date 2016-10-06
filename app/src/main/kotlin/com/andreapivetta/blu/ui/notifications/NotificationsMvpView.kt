package com.andreapivetta.blu.ui.notifications

import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 28/07/16.
 */
interface NotificationsMvpView : MvpView {

    fun hideEmptyMessage()

    fun showNotifications(readNotifications: List<Notification>,
                          unreadNotifications: List<Notification>)

}