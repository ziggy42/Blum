package com.andreapivetta.blu.data.storage

/**
 * Created by andrea on 17/09/16.
 */
object AppStorageFactory {

    fun getAppStorage() = RealmAppStorage()

}