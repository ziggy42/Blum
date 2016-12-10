package com.andreapivetta.blu.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.jobs.NotificationsIntentService
import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.ui.custom.Theme
import com.andreapivetta.blu.ui.custom.decorators.SpaceTopItemDecoration

/**
 * Created by andrea on 28/07/16.
 */
class NotificationsFragment : Fragment(), NotificationsMvpView {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private val presenter by lazy { NotificationsPresenter(AppStorageFactory.getAppStorage()) }
    private val receiver: NotificationUpdatesReceiver? by lazy { NotificationUpdatesReceiver() }
    private var adapter = NotificationsAdapter()
    private lateinit var emptyView: ViewGroup

    override fun onResume() {
        super.onResume()
        activity.registerReceiver(receiver, IntentFilter(Notification.NEW_NOTIFICATION_INTENT))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_notifications, container, false)
        val recyclerView = rootView?.findViewById(R.id.notificationsRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Utils.dpToPx(context, 10)))
        recyclerView.adapter = adapter

        val refreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        refreshLayout.setColorSchemeColors(Theme.getColorPrimary(context))
        refreshLayout.setOnRefreshListener {
            NotificationsIntentService.startService(context)
            Toast.makeText(context, R.string.checking_notifications, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ refreshLayout.isRefreshing = false }, 2000)
        }

        emptyView = rootView?.findViewById(R.id.emptyLinearLayout) as ViewGroup

        presenter.getNotifications()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unregisterReceiver(receiver)
    }

    override fun showNotifications(readNotifications: List<Notification>,
                                   unreadNotifications: List<Notification>) {
        adapter.unreadNotifications = unreadNotifications
        adapter.readNotifications = readNotifications
        adapter.notifyDataSetChanged()
    }

    override fun hideEmptyMessage() {
        emptyView.visible(false)
    }

    inner class NotificationUpdatesReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action === Notification.NEW_NOTIFICATION_INTENT) {
                presenter.onNewNotification()
            }
        }
    }
}