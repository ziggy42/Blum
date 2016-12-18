package com.andreapivetta.blu.ui.privatemessages.list

import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 28/07/16.
 */
interface PrivateMessagesListMvpView : MvpView {

    fun showConversations(conversations: MutableList<PrivateMessage>)

    fun showError()

    fun showEmpty()

}