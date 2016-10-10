package com.andreapivetta.blu.ui.newtweet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.FileUtils
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.Tweet
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import kotlinx.android.synthetic.main.activity_newtweet.*
import kotlinx.android.synthetic.main.quoted_tweet.*
import java.io.File

class NewTweetActivity : AppCompatActivity(), NewTweetMvpView {

    private val presenter: NewTweetPresenter = NewTweetPresenter()

    private val adapter: DeletableImageAdapter by lazy { DeletableImageAdapter(mutableListOf()) }

    private var quotedTweet: Tweet? = null

    companion object {
        private val TAG_USER_PREFIX = "userPref"
        private val TAG_REPLY_ID = "replyId"
        private val TAG_QUOTED_TWEET = "quotedStatus"

        fun launch(context: Context) {
            context.startActivity(Intent(context, NewTweetActivity::class.java))
        }

        fun launch(context: Context, userPref: String) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_USER_PREFIX, userPref)
            context.startActivity(intent)
        }

        fun launch(context: Context, tweet: Tweet) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_QUOTED_TWEET, tweet)
            context.startActivity(intent)
        }

        fun launch(context: Context, userPref: String, replyId: Long) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_USER_PREFIX, userPref)
                    .putExtra(TAG_REPLY_ID, replyId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtweet)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener { finish() }
        presenter.attachView(this)

        newTweetEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.afterTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        if (intent.hasExtra(TAG_QUOTED_TWEET))
            setupQuoted()
        else
            setup()

        if (intent.hasExtra(TAG_USER_PREFIX)) {
            newTweetEditText.setText("${intent.getStringExtra(TAG_USER_PREFIX)} ")
            newTweetEditText.setSelection(newTweetEditText.text.length)
            presenter.afterTextChanged(newTweetEditText.text.toString())
        }

        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if ("text/plain" == intent.type) {
                newTweetEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
                newTweetEditText.setSelection(newTweetEditText.text.length)
                presenter.afterTextChanged(newTweetEditText.text.toString())
            } else if (intent.type.startsWith("image/")) {
                val selectedImageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                adapter.imageFiles.add(File(FileUtils.getPath(this, selectedImageUri)))
                photosRecyclerView.visible()
                adapter.notifyDataSetChanged()
                presenter.afterTextChanged(newTweetEditText.text.toString())
            }
        } else if (Intent.ACTION_SEND_MULTIPLE == intent.action && intent.type != null) {
            if (intent.type.startsWith("image/")) {
                val imageUris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                if (imageUris != null) {
                    photosRecyclerView.visible()
                    for (i in imageUris.indices) {
                        if (i >= 4) {
                            showTooManyImagesError()
                            break
                        }
                        adapter.imageFiles.add(File(FileUtils.getPath(this, imageUris[i])))
                    }
                    adapter.notifyDataSetChanged()
                    presenter.afterTextChanged(newTweetEditText.text.toString())
                }
            }
        }
    }

    private fun setup() {
        photosRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        photosRecyclerView.adapter = adapter

        photoImageButton.setOnClickListener { presenter.takePicture(adapter.itemCount) }
        imageImageButton.setOnClickListener { presenter.grabImage(adapter.itemCount) }
    }

    private fun setupQuoted() {
        quotedStatusLayout.visible()
        photosRecyclerView.visible(false)
        photoImageButton.visible(false)
        imageImageButton.visible(false)

        quotedTweet = intent.getSerializableExtra(TAG_QUOTED_TWEET) as Tweet
        quotedUserNameTextView.text = quotedTweet?.user?.name
        quotedStatusTextView.text = quotedTweet?.text
        if (quotedTweet != null && quotedTweet!!.hasSingleImage()) {
            photoImageView.visible()
            photoImageView.loadUrl(quotedTweet!!.getImageUrl())
        }
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
            if (intent.hasExtra(TAG_REPLY_ID))
                presenter.reply(intent.getLongExtra(TAG_REPLY_ID, -1), adapter.imageFiles)
            else if (intent.hasExtra(TAG_QUOTED_TWEET))
                presenter.sendTweet(quotedTweet!!)
            else
                presenter.sendTweet(adapter.imageFiles)
        return true
    }

    override fun getTweet() = newTweetEditText.text.toString()

    override fun showTooManyCharsError() {
        Toast.makeText(this, R.string.too_many_characters, Toast.LENGTH_SHORT).show()
    }

    override fun showTooManyImagesError() {
        Toast.makeText(this, R.string.too_many_images, Toast.LENGTH_SHORT).show()
    }

    override fun showSendTweetError() {
        Toast.makeText(this, R.string.sending_message_error, Toast.LENGTH_SHORT).show()
    }

    override fun refreshToolbar() {
        invalidateOptionsMenu()
    }

    override fun close() {
        finish()
    }

    override fun takePicture() {
        RxImagePicker.with(applicationContext).requestImage(Sources.CAMERA)
                .flatMap {
                    RxImageConverters.uriToFile(applicationContext, it, File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}_image.jpeg"))
                }
                .subscribe {
                    adapter.imageFiles.add(it)
                    adapter.notifyDataSetChanged()
                }
    }

    override fun grabImage() {
        RxImagePicker.with(applicationContext).requestImage(Sources.GALLERY)
                .flatMap {
                    RxImageConverters.uriToFile(applicationContext, it, File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}_image.jpeg"))
                }
                .subscribe {
                    adapter.imageFiles.add(it)
                    adapter.notifyDataSetChanged()
                }
    }

}
