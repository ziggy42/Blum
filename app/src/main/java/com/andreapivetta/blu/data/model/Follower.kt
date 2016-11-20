package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Follower(@PrimaryKey open var userId: Long = 0) : RealmObject() {

    override fun equals(other: Any?) = if (other !is Follower) false else userId == other.userId

    override fun hashCode(): Int = 17 + 31 * userId.hashCode()

}