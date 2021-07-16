/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 12:31
 */

package ru.zzemlyanaya.core.activity

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import ru.zzemlyanaya.core.dialog.ErrorView
import ru.zzemlyanaya.core.dialog.MessageView
import ru.zzemlyanaya.core.dialog.ProgressView
import ru.zzemlyanaya.core.utils.KeyboardUtils
import javax.inject.Inject
import javax.inject.Named

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    @field:Named("")
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: GlobalRouter
    protected open val navigator: Navigator? = null

    @Inject
    lateinit var keyboardUtils: KeyboardUtils

    private var mProgress: ProgressView? = null
    private var mError: ErrorView? = null
    private var mMessage: MessageView? = null

    private var lastToast: Toast? = null


    override fun onPause() {
        //navigatorHolder.removeNavigator()
        super.onPause()
        if (isFinishing) {
            hideProgress()
        }
    }

    fun showError() {
        mError?.let {
            if (mError?.isShowing == true) {
                mError?.dismiss()
            }
        }
        getErrorWindow()
        mError?.show()
    }

    fun hideError() {
        mError?.dismiss()
    }

    fun showProgress() {
        mProgress?.let {
            if (mProgress?.isShowing == true) {
                mProgress?.dismiss()
            }
        }
        getProgressWindow()
        mProgress?.show()
    }

    fun hideProgress() {
        mProgress?.dismiss()
    }

    fun showMessage() {
        mMessage?.let {
            if (mMessage?.isShowing == true) {
                mMessage?.dismiss()
            }
        }
        getMessageWindow()
        mMessage?.show()
    }

    fun hideMessage() {
        mMessage?.dismiss()
    }

    private fun getProgressWindow(): ProgressView {
        mProgress = ProgressView(this)
        return mProgress!!
    }

    private fun getErrorWindow(): ErrorView {
        mError = ErrorView(this)
        return mError!!
    }

    private fun getMessageWindow(): MessageView {
        mMessage = MessageView(this)
        return mMessage!!
    }

    fun showToast(@StringRes resId: Int) {
        cancelToast()
        lastToast = Toast.makeText(this, resId, Toast.LENGTH_LONG).apply { show() }
    }

    fun showToast(text: String) {
        cancelToast()
        lastToast = Toast.makeText(this, text, Toast.LENGTH_LONG).apply { show() }
    }

    private fun cancelToast() {
        lastToast?.cancel()
        lastToast = null
    }

    fun hideKeyboard() {
        window.decorView.postDelayed(
            { keyboardUtils.hideKeyboard(window.decorView) },
            KEYBOARD_HIDING_DELAY
        )
    }

    override fun attachBaseContext(newBase: Context?) {
        val updatedContext = updateFontScale(newBase)
        super.attachBaseContext(updatedContext)
    }

    private fun updateFontScale(context: Context?): Context? {
        val configuration = Configuration(context?.resources?.configuration)
        configuration.fontScale = NORMAL_FONT_SCALE
        return context?.createConfigurationContext(configuration)
    }

    companion object {
        private const val KEYBOARD_HIDING_DELAY = 300L
        private const val NORMAL_FONT_SCALE = 1f
    }
}