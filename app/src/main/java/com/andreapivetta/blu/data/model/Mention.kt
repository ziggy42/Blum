package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import twitter4j.Status

open class Mention(open var tweetId: Long = 0, open var userId: Long = 0,
                   open var timestamp: Long = 0) : RealmObject() {

    companion object {
        fun valueOf(status: Status) = Mention(status.id, status.user.id, status.createdAt.time)
    }

    override fun equals(other: Any?) = if (other !is Mention) false else
        tweetId == other.tweetId && userId == other.userId && timestamp == other.timestamp

    override fun hashCode(): Int {
        var result = 17
        result += 31 * result + tweetId.hashCode()
        result += 31 * result + userId.hashCode()
        result += 31 * result + timestamp.hashCode()
        return result
    }

}