package com.andreapivetta.blu.ui.privatemessages

import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 28/07/16.
 */
interface PrivateMessagesMvpView : MvpView {

    fun showConversations(conversations: MutableList<PrivateMessage>)

    fun showError()

    fun showEmpty()

}