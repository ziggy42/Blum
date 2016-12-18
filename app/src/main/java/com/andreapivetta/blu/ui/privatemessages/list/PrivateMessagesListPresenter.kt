package com.andreapivetta.blu.ui.privatemessages.list

import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 06/08/16.
 */
class PrivateMessagesListPresenter(private val storage: AppStorage) : BasePresenter<PrivateMessagesListMvpView>() {

    fun getConversations() {
        val conversations = storage.getConversations()
        if (conversations.size == 0)
            mvpView?.showEmpty()
        else
            mvpView?.showConversations(conversations)
    }

    fun onNewPrivateMessages() {
        mvpView?.showConversations(storage.getConversations())
    }

}