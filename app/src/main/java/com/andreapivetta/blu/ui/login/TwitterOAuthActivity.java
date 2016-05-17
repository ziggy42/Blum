package com.andreapivetta.blu.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andreapivetta.blu.BuildConfig;
import com.andreapivetta.blu.R;
import com.andreapivetta.blu.twitter.TwitterUtils;
import com.andreapivetta.twitterloginview.TwitterLoginListener;
import com.andreapivetta.twitterloginview.TwitterLoginView;

import twitter4j.auth.AccessToken;

public class TwitterOAuthActivity extends Activity implements TwitterLoginListener {

    private TwitterLoginView view;
    private boolean oauthStarted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new TwitterLoginView(this);

        setContentView(view);

        oauthStarted = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (oauthStarted)
            return;

        oauthStarted = true;

        view.start(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET,
                BuildConfig.TWITTER_CALLBACK, this);
    }

    public void onSuccess(AccessToken accessToken) {
        PreferenceManager.getDefaultSharedPreferences(TwitterOAuthActivity.this).edit().
                putString(getString(R.string.pref_oauth_token), accessToken.getToken()).
                putString(getString(R.string.pref_oauth_token_secret), accessToken.getTokenSecret()).
                putBoolean(getString(R.string.pref_key_login), true).
                putLong(getString(R.string.pref_key_logged_user), accessToken.getUserId()).
                apply();

        showMessage(getString(R.string.authorized_by, accessToken.getScreenName()));

        setResult(RESULT_OK, new Intent());
        finish();
    }

    public void onFailure(int resultCode) {
        if (resultCode == TwitterLoginView.CANCELLATION)
            showMessage(getString(R.string.failed_due, getString(R.string.cancellation)));
        else
            showMessage(getString(R.string.failed_due, getString(R.string.error)));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
