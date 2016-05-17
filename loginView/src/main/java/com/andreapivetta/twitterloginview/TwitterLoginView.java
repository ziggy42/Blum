package com.andreapivetta.twitterloginview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterLoginView extends WebView {

    public final static int SUCCESS = 0;
    public final static int CANCELLATION = 1;
    public final static int REQUEST_TOKEN_ERROR = 2;
    public final static int AUTHORIZATION_ERROR = 3;
    public final static int ACCESS_TOKEN_ERROR = 4;

    private TwitterOAuthTask twitterOAuthTask;

    public TwitterLoginView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public TwitterLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TwitterLoginView(Context context) {
        super(context);

        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        WebSettings settings = getSettings();

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    }

    public void start(@NonNull String consumerKey, @NonNull String consumerSecret, @NonNull String callbackUrl,
                      @NonNull TwitterLoginListener listener) {

        TwitterOAuthTask oldTask;
        TwitterOAuthTask newTask;

        synchronized (this) {
            oldTask = twitterOAuthTask;
            newTask = new TwitterOAuthTask();
            twitterOAuthTask = newTask;
        }

        cancelTask(oldTask);

        newTask.execute(consumerKey, consumerSecret, callbackUrl, listener);
    }

    public void cancel() {
        TwitterOAuthTask task;

        synchronized (this) {
            task = twitterOAuthTask;
            twitterOAuthTask = null;
        }

        cancelTask(task);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private void cancelTask(TwitterOAuthTask task) {
        if (task == null)
            return;

        if (!task.isCancelled())
            task.cancel(true);

        synchronized (task) {
            task.notify();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    private class TwitterOAuthTask extends AsyncTask<Object, Void, Integer> {

        private String callbackUrl;
        private TwitterLoginListener listener;
        private Twitter twitter;
        private RequestToken requestToken;
        private AccessToken accessToken;
        private volatile boolean authorizationDone;
        private volatile String verifier;

        @Override
        protected void onPreExecute() {
            TwitterLoginView.this.setWebViewClient(new LocalWebViewClient());
        }

        @Override
        protected Integer doInBackground(Object... args) {
            if (isCancelled())
                return CANCELLATION;

            String consumerKey = (String) args[0];
            String consumerSecret = (String) args[1];
            callbackUrl = (String) args[2];
            listener = (TwitterLoginListener) args[3];

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, consumerSecret);

            try {
                requestToken = twitter.getOAuthRequestToken();
            } catch (TwitterException e) {
                e.printStackTrace();
                return REQUEST_TOKEN_ERROR;
            }

            publishProgress();

            boolean cancelled = waitForAuthorization();
            if (cancelled || isCancelled())
                return CANCELLATION;

            if (verifier == null)
                return AUTHORIZATION_ERROR;

            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            } catch (TwitterException e) {
                e.printStackTrace();
                return ACCESS_TOKEN_ERROR;
            }

            return SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (isCancelled())
                return;

            TwitterLoginView.this.loadUrl(requestToken.getAuthorizationURL());
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == null)
                result = CANCELLATION;

            if (result == SUCCESS) {
                onAuthSuccess();
            } else {
                onAuthFailure(result);
            }

            clearTaskReference();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            onAuthFailure(CANCELLATION);

            clearTaskReference();
        }

        private void onAuthSuccess() {
            listener.onSuccess(accessToken);
        }

        private void onAuthFailure(Integer result) {
            listener.onFailure(result);
        }

        private void clearTaskReference() {
            synchronized (TwitterLoginView.this) {
                if (TwitterLoginView.this.twitterOAuthTask == this) {
                    TwitterLoginView.this.twitterOAuthTask = null;
                }
            }
        }

        private boolean waitForAuthorization() {
            while (!authorizationDone) {
                if (isCancelled())
                    return true;

                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return false;
        }

        private void notifyAuthorization() {
            authorizationDone = true;

            synchronized (this) {
                this.notify();
            }
        }

        private class LocalWebViewClient extends WebViewClient {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                notifyAuthorization();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith(callbackUrl))
                    return false;

                verifier = Uri.parse(url).getQueryParameter("oauth_verifier");

                notifyAuthorization();

                return true;
            }
        }
    }
}
