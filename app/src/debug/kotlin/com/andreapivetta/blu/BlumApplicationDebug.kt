package com.andreapivetta.blu

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by andrea on 18/05/16.
 */
class BlumApplicationDebug : BlumApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        LeakCanary.install(this)
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                        .databaseNamePattern(Pattern.compile("blumRealm"))
                        .build())
                .build())
    }

}