package com.andreapivetta.blu.ui.conversation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.ui.custom.ThemedActivity
import kotlinx.android.synthetic.main.activity_conversation.*
import timber.log.Timber
import twitter4j.User

class ConversationActivity : ThemedActivity(), ConversationMvpView {

    companion object {
        const val ARG_OTHER_ID = "other_id"

        fun launch(context: Context, otherId: Long) {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(ARG_OTHER_ID, otherId)
            context.startActivity(intent)
        }
    }

    private val receiver: PrivateMessagesReceiver? by lazy { PrivateMessagesReceiver() }
    private val presenter: ConversationPresenter by lazy {
        ConversationPresenter(AppStorageFactory.getAppStorage(),
                intent.getLongExtra(ARG_OTHER_ID, -1L))
    }

    private val adapter = ConversationAdapter()

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(PrivateMessage.NEW_PRIVATE_MESSAGE_INTENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        conversationRecyclerView.layoutManager = linearLayoutManager
        conversationRecyclerView.adapter = adapter
        conversationRecyclerView.setHasFixedSize(true)

        sendMessageImageButton.setOnClickListener {
            presenter.sendPrivateMessage(messageEditText.text.toString())
            messageEditText.setText("")
        }

        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        unregisterReceiver(receiver)
    }

    override fun showLoading() {
        loadingProgressBar.visible()
        conversationRecyclerView.visible(false)
    }

    override fun hideLoading() {
        loadingProgressBar.visible(false)
        conversationRecyclerView.visible(true)
    }

    override fun showError() {
        Toast.makeText(this, getString(R.string.cant_find_user), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showUserData(user: User) {
        toolbar.title = user.name
    }

    override fun showConversation(messages: MutableList<PrivateMessage>) {
        adapter.messages = messages
        adapter.notifyDataSetChanged()
    }

    override fun showNewPrivateMessage() {
        adapter.notifyItemInserted(adapter.messages.size)
        conversationRecyclerView.scrollToPosition(adapter.messages.size)
    }

    override fun showSendFailed() {
        Toast.makeText(this, getString(R.string.sending_message_error), Toast.LENGTH_SHORT).show()
    }

    inner class PrivateMessagesReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action === PrivateMessage.NEW_PRIVATE_MESSAGE_INTENT) {
                Timber.i(intent.toString())
                presenter.onNewPrivateMessage()
            }
        }
    }
}
