package com.andreapivetta.blu.ui.profile

import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TimelineMvpView
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
interface UserMvpView : TimelineMvpView, InteractionListener {

    fun follow()

    fun unfollow()

    fun setupUser(user: User)
}