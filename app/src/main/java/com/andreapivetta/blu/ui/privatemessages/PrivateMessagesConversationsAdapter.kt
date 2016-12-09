package com.andreapivetta.blu.ui.privatemessages

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.ui.conversation.ConversationActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import kotlinx.android.synthetic.main.item_conversation.view.*
import java.util.*

/**
 * Created by andrea on 06/08/16.
 */
class PrivateMessagesConversationsAdapter() :
        RecyclerView.Adapter<PrivateMessagesConversationsAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        val userProfilePicImageView: ImageView = rootView.userProfilePicImageView
        val userNameTextView: TextView = rootView.userNameTextView
        val messageTextView: TextView = rootView.messageTextView
        val timeTextView: TextView = rootView.timeTextView
    }

    var messages: MutableList<PrivateMessage> = ArrayList()

    override fun onBindViewHolder(holder: ConversationViewHolder?, position: Int) {
        val message = messages[position]
        val context = holder?.itemView?.context

        holder?.userProfilePicImageView?.loadAvatar(message.otherUserProfilePicUrl)
        holder?.userNameTextView?.text = message.otherUserName
        holder?.messageTextView?.text = if (message.senderId == message.otherId) message.text else
            context?.getString(R.string.you_message, message.text)
        holder?.timeTextView?.text = Utils.formatDate(message.timeStamp)
        holder?.messageTextView?.setTypeface(null, if (message.isRead) Typeface.NORMAL else Typeface.BOLD)

        holder?.rootView?.setOnClickListener {
            ConversationActivity.launch(context!!, message.otherId)
        }

        holder?.userProfilePicImageView
                ?.setOnClickListener { UserActivity.launch(context!!, message.otherId) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ConversationViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.item_conversation, parent, false))

    override fun getItemCount() = messages.size

}