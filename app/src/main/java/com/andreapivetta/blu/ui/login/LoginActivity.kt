package com.andreapivetta.blu.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl

class LoginActivity : Activity(), LoginMvpView {

    private val loginButton: Button by lazy { findViewById(R.id.login_button) as Button }
    private val presenter: LoginPresenter by lazy { LoginPresenter(AppSettingsImpl(applicationContext)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter.attachView(this)
        loginButton.setOnClickListener { presenter.performLogin() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LoginPresenter.CODE_OAUTH)
            if (resultCode == RESULT_OK)
                presenter.onResultOk()
            else
                presenter.onResultCanceled()
    }

    override fun showOauthActivity(requestCode: Int) {
        startActivityForResult(Intent(this, TwitterOAuthActivity::class.java), requestCode)
    }

    override fun showLoginError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG)
                .show()
    }

    override fun showLoginCanceled() {
        Toast.makeText(this, getString(R.string.cancellation), Toast.LENGTH_LONG)
                .show()
    }

    override fun moveOn() {
        // TODO clear task, open MainActivity
        Log.i("LoginView", "moveOn")
    }

}
