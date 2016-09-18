package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import twitter4j.User

open class UserFollowed(
        @PrimaryKey open var id: Long = 0,
        open var name: String = "",
        open var screenName: String = "",
        open var profilePicUrl: String = "") : RealmObject() {

    companion object {
        fun valueOf(user: User) =
                UserFollowed(user.id, user.name, user.screenName, user.biggerProfileImageURL)
    }

}