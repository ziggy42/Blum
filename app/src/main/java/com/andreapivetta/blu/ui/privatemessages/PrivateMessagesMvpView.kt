package com.andreapivetta.blu.ui.privatemessages

import com.andreapivetta.blu.data.model.UserFollowed
import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 16/12/16.
 */
interface PrivateMessagesMvpView : MvpView {

    fun showConversationDialog(users: List<UserFollowed>)

    fun startConversation(userId: Long)

}