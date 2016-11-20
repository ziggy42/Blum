package com.andreapivetta.blu.ui.custom

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory

/**
 * Created by andrea on 20/11/16.
 */
object Theme {

    fun setTheme(activity: Activity) =
            when (AppSettingsFactory.getAppSettings(activity).getTheme()) {
                "B" -> activity.setTheme(R.style.BlueAppTheme)
                "P" -> activity.setTheme(R.style.PinkAppTheme)
                "G" -> activity.setTheme(R.style.GreenAppTheme)
                "D" -> activity.setTheme(R.style.DarkTheme)
                else -> throw UnsupportedOperationException("No such theme")
            }

    fun getColorPrimary(context: Context) =
            ContextCompat.getColor(context, getColorPrimaryId(context))

    fun getColorPrimaryId(context: Context) =
            when (AppSettingsFactory.getAppSettings(context).getTheme()) {
                "B" -> R.color.blueThemeColorPrimary
                "P" -> R.color.pinkThemeColorPrimary
                "G" -> R.color.greenThemeColorPrimary
                "D" -> R.color.darkThemeColorPrimary
                else -> throw RuntimeException("No such theme")
            }
}