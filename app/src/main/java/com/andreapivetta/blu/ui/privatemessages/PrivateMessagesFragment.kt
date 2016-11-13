package com.andreapivetta.blu.ui.privatemessages

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorageFactory

/**
 * Created by andrea on 28/07/16.
 */
class PrivateMessagesFragment : Fragment(), PrivateMessagesMvpView {

    companion object {
        fun newInstance() = PrivateMessagesFragment()
    }

    private val presenter by lazy { PrivateMessagesPresenter(AppStorageFactory.getAppStorage()) }

    private lateinit var adapterPrivateMessages: PrivateMessagesConversationsAdapter
    private lateinit var emptyViewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        adapterPrivateMessages = PrivateMessagesConversationsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_privatemessages, container, false)

        val recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        emptyViewGroup = rootView?.findViewById(R.id.emptyLinearLayout) as ViewGroup

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterPrivateMessages

        return rootView
    }

    override fun onResume() {
        super.onResume()
        presenter.getConversations()
    }

    override fun showConversations(conversations: MutableList<PrivateMessage>) {
        adapterPrivateMessages.dataSet = conversations
        adapterPrivateMessages.notifyDataSetChanged()
    }

    override fun showError() {
        emptyViewGroup.visible()
    }

    override fun showEmpty() {
        emptyViewGroup.visible()
    }

}