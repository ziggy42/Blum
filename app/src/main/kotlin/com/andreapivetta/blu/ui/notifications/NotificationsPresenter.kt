package com.andreapivetta.blu.ui.notifications

import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 30/09/16.
 */
class NotificationsPresenter(val storage: AppStorage) : BasePresenter<NotificationsMvpView>() {

    fun getNotifications() {
        val notifications = storage.getAllNotifications()
        if (notifications.size > 0) {
            mvpView?.showNotifications(notifications)
            mvpView?.hideEmptyMessage()
        }
    }
}