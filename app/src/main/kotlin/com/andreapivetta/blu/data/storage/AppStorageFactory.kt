package com.andreapivetta.blu.data.storage

import android.content.Context

/**
 * Created by andrea on 17/09/16.
 */
object AppStorageFactory {

    fun getAppStorage(context: Context) = RealmAppStorage(context)

}