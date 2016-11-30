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

    fun hasFavoriters(): Boolean = favoriters != null && favoriters!!.isNotEmpty()

    fun hasRetweeters(): Boolean = retweeters != null && retweeters!!.isNotEmpty()

    override fun equals(other: Any?): Boolean = if (other !is TweetInfo) false else id == other.id

    override fun hashCode(): Int = (id * 17).toInt()

}