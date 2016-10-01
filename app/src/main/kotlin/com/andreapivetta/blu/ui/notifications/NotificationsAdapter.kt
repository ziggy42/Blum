package com.andreapivetta.blu.ui.notifications

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.data.model.Notification
import kotlinx.android.synthetic.main.notification_item.view.*
import java.util.*

/**
 * Created by andrea on 29/09/16.
 */
class NotificationsAdapter() : RecyclerView.Adapter<
        NotificationsAdapter.Companion.NotificationViewHolder>() {

    companion object {

        private val TYPE_ITEM = 0

        class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun setup(notification: Notification) {
                itemView.userProfilePicImageView.loadUrl(notification.profilePicURL)
                itemView.notificationTypeImageView.setImageDrawable(
                        NotificationAssetsSelector.getIcon(notification, itemView.context))

                itemView.userNameTextView.text = notification.userName
                itemView.timeTextView.text = Utils
                        .formatDate(notification.timestamp, itemView.context)
                itemView.notificationTextView.text = NotificationAssetsSelector
                        .getText(notification, itemView.context)
            }

        }

    }

    var dataSet: List<Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NotificationViewHolder =
            NotificationViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.notification_item, parent, false))

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: NotificationViewHolder?, position: Int) {
        holder?.setup(dataSet[position])
    }

    override fun getItemViewType(position: Int) = TYPE_ITEM
}