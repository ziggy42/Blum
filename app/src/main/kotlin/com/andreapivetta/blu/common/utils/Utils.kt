package com.andreapivetta.blu.common.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import com.andreapivetta.blu.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {

    fun dpToPx(context: Context, dp: Int) = Math.round(dp *
            (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

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
                        return SimpleDateFormat("MMM dd", Locale.getDefault()).format(c2.time)
                    else
                        return SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(c2.time)
                } else
                    return context?.getString(R.string.mini_hours, hours.toInt())
            } else
                return context?.getString(R.string.mini_minutes, minutes.toInt())
        } else
            return context?.getString(R.string.mini_seconds, seconds.toInt())
    }

    fun runOnUiThread(body: () -> Unit) {
        Handler(Looper.getMainLooper()).post { body.invoke() }
    }

}
