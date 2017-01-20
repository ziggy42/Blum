package com.andreapivetta.blu.ui.conversation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.setupText
import com.andreapivetta.blu.data.model.PrivateMessage
import com.luseen.autolinklibrary.AutoLinkTextView
import kotlinx.android.synthetic.main.message_left.view.*

/**
 * Created by andrea on 21/09/16.
 */
class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.PrivateMessageViewHolder>() {

    var messages: MutableList<PrivateMessage> = mutableListOf()

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
        private val messageTextView: AutoLinkTextView = container.messageTextView
        private val timeTextView: TextView = container.timeTextView

        fun setup(privateMessage: PrivateMessage) {
            messageTextView.setupText(privateMessage.text)
            timeTextView.text = Utils.formatDate(privateMessage.timeStamp)
        }
    }
}