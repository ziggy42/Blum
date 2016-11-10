package com.andreapivetta.blu.ui.notifications

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.data.model.Notification
import kotlinx.android.synthetic.main.notification_item.view.*
import kotlinx.android.synthetic.main.notification_item_header.view.*
import java.util.*

/**
 * Created by andrea on 29/09/16.
 */
class NotificationsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private val TYPE_HEADER_NEW = 0
        private val TYPE_HEADER_OLD = 1
        private val TYPE_NOTIFICATION_NEW = 2
        private val TYPE_NOTIFICATION_OLD = 3

        class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun setup(notification: Notification) {
                itemView.userProfilePicImageView.loadAvatar(notification.profilePicURL)
                itemView.notificationTypeImageView.setImageDrawable(
                        NotificationAssetsSelector.getIcon(notification, itemView.context))

                itemView.userNameTextView.text = notification.userName
                itemView.timeTextView.text = Utils
                        .formatDate(notification.timestamp, itemView.context)
                itemView.notificationTextView.text = NotificationAssetsSelector
                        .getText(notification, itemView.context)
            }

        }

        class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun setup(old: Boolean) {
                itemView.labelTextView.text = if (old) "Old" else "New"
            }

        }

    }

    var unreadNotifications: List<Notification> = ArrayList()
    var readNotifications: List<Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == TYPE_NOTIFICATION_NEW || viewType == TYPE_NOTIFICATION_OLD)
                NotificationViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.notification_item, parent, false))
            else
                HeaderViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.notification_item_header, parent, false))

    override fun getItemCount(): Int {
        if (unreadNotifications.isNotEmpty())
            if (readNotifications.isNotEmpty())
                return 2 + readNotifications.size + unreadNotifications.size
            else
                return 1 + unreadNotifications.size
        else
            if (readNotifications.isNotEmpty())
                return 1 + readNotifications.size
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            TYPE_NOTIFICATION_NEW -> (holder as NotificationViewHolder)
                    .setup(unreadNotifications[getRealIndex(position)])
            TYPE_NOTIFICATION_OLD -> (holder as NotificationViewHolder)
                    .setup(readNotifications[getRealIndex(position)])
            TYPE_HEADER_OLD -> (holder as HeaderViewHolder).setup(true)
            TYPE_HEADER_NEW -> (holder as HeaderViewHolder).setup(false)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (unreadNotifications.isNotEmpty()) {
            if (position == 0)
                return TYPE_HEADER_NEW
            else if (position <= unreadNotifications.size)
                return TYPE_NOTIFICATION_NEW
            else if (position == unreadNotifications.size + 1 && unreadNotifications.isNotEmpty())
                return TYPE_HEADER_OLD
            else
                return TYPE_NOTIFICATION_OLD
        } else {
            if (position == 0)
                return TYPE_HEADER_OLD
            else
                return TYPE_NOTIFICATION_OLD
        }
    }

    private fun getRealIndex(position: Int): Int {
        if (getItemViewType(position) == TYPE_NOTIFICATION_NEW)
            return position - 1
        else if (unreadNotifications.isNotEmpty())
            return position - 2 - unreadNotifications.size
        else
            return position - 1
    }
}