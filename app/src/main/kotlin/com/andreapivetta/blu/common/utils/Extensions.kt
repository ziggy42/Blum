package com.andreapivetta.blu.common.utils

import android.view.View

/**
 * Created by andrea on 27/09/16.
 */
fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}