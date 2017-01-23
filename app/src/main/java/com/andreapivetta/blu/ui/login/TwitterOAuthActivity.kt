package com.andreapivetta.blu.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.twitter.Twitter
import com.andreapivetta.twitterloginview.TwitterLoginListener
import com.andreapivetta.twitterloginview.TwitterLoginView
import twitter4j.auth.AccessToken

class TwitterOAuthActivity : Activity(), TwitterLoginListener {

    private val view: TwitterLoginView by lazy { TwitterLoginView(this) }
    private var oauthStarted: Boolean = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()

        if (!oauthStarted) {
            oauthStarted = true
            view.start(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET,
                    BuildConfig.TWITTER_CALLBACK, this)
        }
    }

    override fun onSuccess(accessToken: AccessToken) {
        val settings = AppSettingsFactory.getAppSettings(this)
        settings.saveAccessToken(accessToken)
        Twitter.init(settings)

        showMessage(getString(R.string.authorized_by, accessToken.screenName))

        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    override fun onFailure(resultCode: Int) {
        showMessage(getString(R.string.failed_due, getString(
                if (resultCode == TwitterLoginView.CANCELLATION) R.string.cancellation
                else R.string.error)))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
