package com.andreapivetta.blu.ui.privatemessages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.pushFragment
import com.andreapivetta.blu.data.model.UserFollowed
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.ui.conversation.ConversationActivity
import com.andreapivetta.blu.ui.custom.ThemedActivity
import com.andreapivetta.blu.ui.privatemessages.list.PrivateMessagesListFragment
import kotlinx.android.synthetic.main.activity_private_messages.*
import kotlinx.android.synthetic.main.dialog_select_user.view.*


class PrivateMessagesActivity : ThemedActivity(), PrivateMessagesMvpView {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, PrivateMessagesActivity::class.java))
        }
    }

    private val presenter = PrivateMessagesPresenter(AppStorageFactory.getAppStorage())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_messages)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        fab.setOnClickListener { presenter.onButtonClicked() }

        if (savedInstanceState == null)
            pushFragment(R.id.container_frameLayout, PrivateMessagesListFragment.newInstance())

        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun startConversation(userId: Long) {
        ConversationActivity.launch(this, userId)
    }

    override fun showConversationDialog(users: List<UserFollowed>) {
        val dialogView = View.inflate(this, R.layout.dialog_select_user, null)

        val adapter = UsersAdapter(users, this)
        dialogView.usersRecyclerView.setHasFixedSize(true)
        dialogView.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        dialogView.usersRecyclerView.adapter = adapter
        dialogView.findUserEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                adapter.users = presenter.queryUsers(s.toString())
                adapter.notifyDataSetChanged()
            }
        })

        AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(R.string.cancel, null)
                .create()
                .show()
    }
}
