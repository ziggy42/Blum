package com.andreapivetta.blu.common.pref

/**
 * Created by andrea on 15/05/16.
 */
interface AppSettings {

    fun isUserLoggedIn() : Boolean

    fun setUserLoggedIn(logged: Boolean)

}