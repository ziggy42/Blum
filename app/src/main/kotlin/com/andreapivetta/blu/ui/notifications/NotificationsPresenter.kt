package com.andreapivetta.blu.ui.notifications

import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 30/09/16.
 */
class NotificationsPresenter(val storage: AppStorage) : BasePresenter<NotificationsMvpView>() {

    fun getNotifications() {
        val readNotifications = storage.getReadNotifications()
        val unreadNotifications = storage.getUnreadNotifications()

        if (readNotifications.size > 0 || unreadNotifications.size > 0) {
            mvpView?.showNotifications(readNotifications, unreadNotifications)
            mvpView?.hideEmptyMessage()
        }
    }

    override fun detachView() {
        super.detachView()
        storage.markAllNotificationsAsRead()
    }
}