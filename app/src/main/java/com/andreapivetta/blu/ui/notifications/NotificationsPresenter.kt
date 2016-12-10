package com.andreapivetta.blu.ui.notifications

import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter
import java.util.*

/**
 * Created by andrea on 30/09/16.
 */
class NotificationsPresenter(private val storage: AppStorage) : BasePresenter<NotificationsMvpView>() {

    // TODO performance

    fun getNotifications() {
        val readNotifications = ArrayList(storage.getReadNotifications())
        val unreadNotifications = ArrayList(storage.getUnreadNotifications())

        if (readNotifications.isNotEmpty() || unreadNotifications.isNotEmpty()) {
            mvpView?.showNotifications(readNotifications, unreadNotifications)
            mvpView?.hideEmptyMessage()
            storage.markAllNotificationsAsRead()
        }
    }

    fun onNewNotification() {
        val readNotifications = ArrayList(storage.getReadNotifications())
        val unreadNotifications = ArrayList(storage.getUnreadNotifications())

        mvpView?.showNotifications(readNotifications, unreadNotifications)
        storage.markAllNotificationsAsRead()
    }

}