package com.andreapivetta.blu.ui.usertimeline

import android.os.Bundle
import com.andreapivetta.blu.ui.timeline.TimelineFragment

/**
 * Created by andrea on 25/06/16.
 */
class UserTimelineFragment : TimelineFragment() {

    companion object {
        fun newInstance(userId: Long): UserTimelineFragment {
            val fragment = UserTimelineFragment()
            val bundle = Bundle()
            bundle.putLong(TAG_USER_ID, userId)
            fragment.arguments = bundle
            return fragment
        }

        private val TAG_USER_ID = "userId"
    }

    override fun getTimelinePresenter() = UserTimelinePresenter(arguments.getLong(TAG_USER_ID))
}