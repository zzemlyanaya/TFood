/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:16
 */

package ru.zzemlyanaya.core.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import ru.zzemlyanaya.core.api.model.*
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.dialog.ErrorDialog
import ru.zzemlyanaya.core.dialog.InfoDialog
import ru.zzemlyanaya.core.model.MessageEntity
import ru.zzemlyanaya.core.presentation.BaseViewWithData
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.LoadingView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.core.utils.KeyboardUtils
import toothpick.ktp.KTP
import javax.inject.Inject

abstract class CoreActivity : AppCompatActivity(), BaseViewWithData {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router
    protected open val navigator: Navigator? = null

    @Inject
    lateinit var keyboardUtils: KeyboardUtils

    protected open val mProgress: LoadingView? = null
    protected open var mError: ErrorView? = null
    protected open var mMessage: MessageView? = null

    private var lastToast: Toast? = null

    protected val loadingViewTag = "Progress-${this::class.java.simpleName}"
    protected val errorViewTag = "Error-${this::class.java.simpleName}"
    protected val infoViewTag = "Info-${this::class.java.simpleName}"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        KTP.openScope(APP_SCOPE).inject(this)
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onPause() {
        //navigatorHolder.removeNavigator()
        super.onPause()
        if (isFinishing) {
            mProgress?.hideProgress()
        }
    }

    override fun <T> handleDataState(state: State<T>) {
        when (state) {
            Loading -> onLoading()
            is Error -> onError(state.message)
            Empty -> onEmpty()
            is Success<*> -> onData(state.data)
        }
    }

    override fun onLoading() {
        mProgress?.showProgress()
    }

    override fun onEmpty() {
        mProgress?.hideProgress()
    }

    override fun onError(message: String) {
        mProgress?.hideProgress()
        mError = ErrorDialog
            .view(supportFragmentManager, errorViewTag, message)
            .also { it.showError() }
    }

    override fun <T> onData(data: T) {
        mProgress?.hideProgress()
    }

    fun showMessage(message: MessageEntity) {
        mMessage = InfoDialog
            .view(supportFragmentManager, infoViewTag, message)
            .also { it.showMessage() }
    }

    fun hideMessage() {
        mMessage?.hideMessage()
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

    override fun hideKeyboard() {
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