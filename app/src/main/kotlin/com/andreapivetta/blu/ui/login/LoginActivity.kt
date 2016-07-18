package com.andreapivetta.blu.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginMvpView {

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
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
    }

    override fun showLoginCanceled() {
        Toast.makeText(this, getString(R.string.cancellation), Toast.LENGTH_LONG).show()
    }

    override fun moveOn() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
