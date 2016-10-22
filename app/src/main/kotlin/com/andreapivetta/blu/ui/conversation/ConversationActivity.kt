package com.andreapivetta.blu.ui.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.TwitterUtils
import kotlinx.android.synthetic.main.activity_conversation.*
import twitter4j.User

class ConversationActivity : AppCompatActivity(), ConversationMvpView {

    companion object {
        private val ARG_OTHER_ID = "other_id"

        fun launch(context: Context, otherId: Long) {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(ARG_OTHER_ID, otherId)
            context.startActivity(intent)
        }
    }

    private val presenter: ConversationPresenter by lazy {
        ConversationPresenter(TwitterUtils.getTwitter(), AppStorageFactory.getAppStorage(),
                intent.getLongExtra(ARG_OTHER_ID, -1L))
    }

    private val adapter = ConversationAdapter()

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
        adapter.messages.addAll(messages)
        adapter.notifyDataSetChanged()
    }

    override fun showNewPrivateMessage(message: PrivateMessage) {
        adapter.messages.add(adapter.messages.size - 1, message)
        adapter.notifyDataSetChanged()
    }

    override fun showSendFailed() {
        Toast.makeText(this, getString(R.string.sending_message_error), Toast.LENGTH_SHORT).show()
    }
}
