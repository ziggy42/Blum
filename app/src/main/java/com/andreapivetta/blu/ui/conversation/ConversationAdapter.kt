package com.andreapivetta.blu.ui.conversation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.data.model.PrivateMessage
import java.util.*

/**
 * Created by andrea on 21/09/16.
 */
class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.PrivateMessageViewHolder>() {

    var messages: MutableList<PrivateMessage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PrivateMessageViewHolder =
            PrivateMessageViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(viewType, parent, false))

    override fun onBindViewHolder(holder: PrivateMessageViewHolder?, position: Int) {
        holder?.setup(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        val privateMessage = messages[position]
        return if (privateMessage.otherId == privateMessage.senderId)
            R.layout.message_left else R.layout.message_right
    }

    override fun getItemCount() = messages.size

    class PrivateMessageViewHolder(container: View) : RecyclerView.ViewHolder(container) {
        var messageTextView: TextView = container.findViewById(R.id.messageTextView) as TextView
        var timeTextView: TextView = container.findViewById(R.id.timeTextView) as TextView

        fun setup(privateMessage: PrivateMessage) {
            messageTextView.text = privateMessage.text
            timeTextView.text = Utils.formatDate(privateMessage.timeStamp)
        }
    }
}