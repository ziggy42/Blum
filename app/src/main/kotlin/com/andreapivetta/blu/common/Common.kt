package com.andreapivetta.blu.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object Common {

    fun getBitmapFromURL(strURL: String): Bitmap? {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun dpToPx(context: Context, dp: Int) = Math.round(dp *
            (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

    fun pxToDp(context: Context, px: Int) = Math.round(px /
            (context.resources.displayMetrics.densityDpi.toDouble() / DisplayMetrics.DENSITY_DEFAULT.toDouble())).toInt()

    fun getResourceColorPrimary(context: Context): Int {
        when (AppSettingsImpl.getTheme()) {
            "B" -> return ContextCompat.getColor(context, R.color.blueThemeColorPrimary)
            "P" -> return ContextCompat.getColor(context, R.color.pinkThemeColorPrimary)
            "G" -> return ContextCompat.getColor(context, R.color.greenThemeColorPrimary)
            "D" -> return ContextCompat.getColor(context, R.color.darkThemeColorPrimary)
            else -> return ContextCompat.getColor(context, R.color.blueThemeColorPrimary)
        }
    }

    fun formatDate(timeStamp: Long, context: Context?): String? {
        val c = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c2.timeInMillis = timeStamp

        val diff = c.timeInMillis - timeStamp
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds > 60) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            if (minutes > 60) {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours > 24) {
                    if (c.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                        return java.text.SimpleDateFormat("MMM dd", Locale.getDefault()).format(c2.time)
                    else
                        return java.text.SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(c2.time)
                } else
                    return context?.getString(R.string.mini_hours, hours.toInt())
            } else
                return context?.getString(R.string.mini_minutes, minutes.toInt())
        } else
            return context?.getString(R.string.mini_seconds, seconds.toInt())
    }

}
