package com.andreapivetta.blu.ui.conversation

import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.ui.base.MvpView
import twitter4j.User

/**
 * Created by andrea on 20/09/16.
 */
interface ConversationMvpView : MvpView {

    fun showConversation(messages: MutableList<PrivateMessage>)

    fun showNewPrivateMessage(message: PrivateMessage)

    fun showSendFailed()

    fun showLoading()

    fun hideLoading()

    fun showError()

    fun showUserData(user: User)

}