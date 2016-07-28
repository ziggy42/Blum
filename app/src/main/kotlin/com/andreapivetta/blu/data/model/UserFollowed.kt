package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by andrea on 26/07/16.
 */
open class UserFollowed(@PrimaryKey open var id: Long = 0, open var name: String = "",
                        open var screenName: String = "", open var profilePicUrl: String = "") :
        RealmObject() {

}