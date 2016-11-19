package com.andreapivetta.blu.ui.custom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by andrea on 15/05/16.
 */
open class ThemedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Theme.setTheme(this)
        super.onCreate(savedInstanceState)
    }
}