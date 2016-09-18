package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Follower(@PrimaryKey open var userId: Long = 0) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (other !is Follower)
            return false
        return userId == other.userId
    }

    override fun hashCode(): Int = 17 + 31 * userId.hashCode()

}