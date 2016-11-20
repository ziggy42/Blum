package com.andreapivetta.blu.ui.notifications

import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 30/09/16.
 */
class NotificationsPresenter(private val storage: AppStorage) : BasePresenter<NotificationsMvpView>() {

    fun getNotifications() {
        val readNotifications = storage.getReadNotifications()
        val unreadNotifications = storage.getUnreadNotifications()

        if (readNotifications.isNotEmpty() || unreadNotifications.isNotEmpty()) {
            mvpView?.showNotifications(readNotifications, unreadNotifications)
            mvpView?.hideEmptyMessage()
            storage.markAllNotificationsAsRead()
        }
    }

}