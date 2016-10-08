package com.andreapivetta.blu.ui.newtweet

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by andrea on 08/10/16.
 */
class NewTweetPresenterTest {

    var presenter: NewTweetPresenter? = null

    @Before
    fun setUp() {
        presenter = NewTweetPresenter()
    }

    @Test
    fun charsLeft() {
        presenter?.afterTextChanged("Hello")
        assertTrue(presenter?.charsLeft() == 135)

        presenter?.afterTextChanged("Hello   ")
        assertTrue(presenter?.charsLeft() == 132)

        presenter?.afterTextChanged("H  e ll   o")
        assertTrue(presenter?.charsLeft() == 129)

        presenter?.afterTextChanged("Hello https://github.com/ziggy42/Blum-kotlin")
        assertTrue(presenter?.charsLeft() == 111)
    }

}