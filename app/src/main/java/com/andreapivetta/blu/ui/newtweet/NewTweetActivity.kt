package com.andreapivetta.blu.ui.newtweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.views.EditTextCursorWatcher
import com.andreapivetta.blu.ui.new.NewTweetMvpView

class NewTweetActivity : AppCompatActivity(), NewTweetMvpView {

    private val presenter: NewTweetPresenter = NewTweetPresenter()

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val newTweetEditText: EditTextCursorWatcher by lazy {
        findViewById(R.id.newTweet_editText) as EditTextCursorWatcher
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, NewTweetActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtweet)
        setSupportActionBar(toolbar)
        presenter.attachView(this)

        newTweetEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.afterTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.onTextChanged(s, start, before, count)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_new_tweet, menu)

        val item = menu?.findItem(R.id.action_chars_left)
        MenuItemCompat.setActionView(item, R.layout.menu_chars_left)
        val view = MenuItemCompat.getActionView(item)

        val charsLeft = presenter.charsLeft()
        val charsLeftTextVIew = view.findViewById(R.id.charsLeftTextView) as TextView
        charsLeftTextVIew.text = charsLeft.toString()
        if (charsLeft < 0)
            charsLeftTextVIew.setTextColor(ContextCompat.getColor(this, R.color.red))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_send)
            presenter.sendTweet()
        return true
    }

    override fun getTweet(): String {
        return newTweetEditText.text.toString()
    }

    override fun showTooManyCharsError() {
        AlertDialog.Builder(this)
                .setTitle(R.string.too_many_characters)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    override fun showSendTweetError() {
        Toast.makeText(this, getString(R.string.sending_message_error), Toast.LENGTH_SHORT).show()
    }

    override fun refreshToolbar() {
        invalidateOptionsMenu()
    }

    override fun close() {
        finish()
    }

    override fun takePicture() {
        throw UnsupportedOperationException()
    }

    override fun grabImage() {
        throw UnsupportedOperationException()
    }
}
