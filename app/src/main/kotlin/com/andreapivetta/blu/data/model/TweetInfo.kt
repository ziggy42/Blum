package com.andreapivetta.blu.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by andrea on 01/08/16.
 */
open class TweetInfo(
        @PrimaryKey open var id: Long = 0,
        open var favoriters: RealmList<UserId>? = RealmList<UserId>(),
        open var retweeters: RealmList<UserId>? = RealmList<UserId>()) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (other !is TweetInfo)
            return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return (id * 17).toInt()
    }
}