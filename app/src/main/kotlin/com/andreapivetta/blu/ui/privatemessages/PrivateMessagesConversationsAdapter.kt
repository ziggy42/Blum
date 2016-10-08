package com.andreapivetta.blu.ui.privatemessages

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.ui.conversation.ConversationActivity
import java.util.*

/**
 * Created by andrea on 06/08/16.
 */
class PrivateMessagesConversationsAdapter() :
        RecyclerView.Adapter<PrivateMessagesConversationsAdapter.Companion.ConversationViewHolder>() {

    companion object {

        class ConversationViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
            val userProfilePicImageView =
                    rootView.findViewById(R.id.userProfilePicImageView) as ImageView
            val userNameTextView = rootView.findViewById(R.id.userNameTextView) as TextView
            val messageTextView = rootView.findViewById(R.id.messageTextView) as TextView
            val timeTextView = rootView.findViewById(R.id.timeTextView) as TextView
        }

    }

    var dataSet: MutableList<PrivateMessage> = ArrayList()

    override fun onBindViewHolder(holder: ConversationViewHolder?, position: Int) {
        val message = dataSet[position]
        val context = holder?.itemView?.context

        holder?.userProfilePicImageView?.loadUrl(message.otherUserProfilePicUrl)
        holder?.userNameTextView?.text = message.otherUserName
        holder?.messageTextView?.text = if (message.senderId == message.otherId) message.text else
            context?.getString(R.string.you_message, message.text)
        holder?.timeTextView?.text = Utils.formatDate(message.timeStamp, holder?.rootView?.context)

        holder?.rootView?.setOnClickListener {
            ConversationActivity.launch(context!!, message.otherId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ConversationViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.item_conversation, parent, false))

    override fun getItemCount() = dataSet.size

}