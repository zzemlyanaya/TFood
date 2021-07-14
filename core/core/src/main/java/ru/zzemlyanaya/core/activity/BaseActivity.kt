/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.07.2021, 15:57
 */

package ru.zzemlyanaya.core.activity

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import ru.zzemlyanaya.core.dialog.ProgressView
import ru.zzemlyanaya.core.utils.KeyboardUtils
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

//    @Inject
//    @field:Named("")
//    lateinit var navigatorHolder: NavigatorHolder
//
//    @Inject
//    lateinit var router: GlobalRouter
//    protected open val navigator: Navigator? = null

    @Inject
    lateinit var keyboardUtils: KeyboardUtils

    private var mProgress: ProgressView? = null

    private var lastToast: Toast? = null


    override fun onPause() {
        //navigatorHolder.removeNavigator()
        super.onPause()
        if (isFinishing) {
            hideProgress()
        }
    }

    fun showError() {}

    fun hideError() {}

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

    private fun getProgressWindow(): ProgressView {
        mProgress = ProgressView(this)
        return mProgress!!
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