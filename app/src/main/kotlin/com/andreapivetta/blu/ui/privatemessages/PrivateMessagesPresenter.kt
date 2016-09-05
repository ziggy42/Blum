package com.andreapivetta.blu.ui.privatemessages

import com.andreapivetta.blu.data.db.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 06/08/16.
 */
class PrivateMessagesPresenter(val storage: AppStorage) : BasePresenter<PrivateMessagesMvpView>() {

    fun getConversations() {
        val conversations = storage.getConversations()
        if (conversations.size == 0)
            mvpView?.showEmpty()
        else
            mvpView?.showConversations(conversations)
    }

}