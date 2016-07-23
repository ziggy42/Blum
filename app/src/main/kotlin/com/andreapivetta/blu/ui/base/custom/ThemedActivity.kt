package com.andreapivetta.blu.ui.base.custom

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.andreapivetta.blu.R

/**
 * Created by andrea on 15/05/16.
 */
open class ThemedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        when (PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_key_themes), "B")) {
            "B" -> setTheme(R.style.BlueAppTheme)
            "P" -> setTheme(R.style.PinkAppTheme)
            "G" -> setTheme(R.style.GreenAppTheme)
            "D" -> setTheme(R.style.DarkTheme)
            else -> throw UnsupportedOperationException("No such theme")
        }
        super.onCreate(savedInstanceState)
    }
}