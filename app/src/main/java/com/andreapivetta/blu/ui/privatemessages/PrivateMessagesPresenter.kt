package com.andreapivetta.blu.ui.privatemessages

import com.andreapivetta.blu.data.model.UserFollowed
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.ui.base.BasePresenter

/**
 * Created by andrea on 16/12/16.
 */
class PrivateMessagesPresenter(storage: AppStorage) : BasePresenter<PrivateMessagesMvpView>() {

    private var users: List<UserFollowed> = storage.getAllUserFollowed()

    fun onButtonClicked() {
        checkViewAttached()
        mvpView?.showConversationDialog(users)
    }

    fun queryUsers(query: String) = users
            .filter { x -> x.name.toLowerCase().startsWith(query.toLowerCase()) }

}