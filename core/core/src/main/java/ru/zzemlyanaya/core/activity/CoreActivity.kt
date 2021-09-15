/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.core.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import ru.zzemlyanaya.core.R
import ru.zzemlyanaya.core.dialog.ErrorDialog
import ru.zzemlyanaya.core.dialog.InfoDialog
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.core.model.MessageEntity
import ru.zzemlyanaya.core.network.model.Empty
import ru.zzemlyanaya.core.network.model.Loading
import ru.zzemlyanaya.core.network.model.State
import ru.zzemlyanaya.core.network.model.Success
import ru.zzemlyanaya.core.presentation.BaseViewWithData
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.LoadingView
import ru.zzemlyanaya.core.presentation.MessageView

abstract class CoreActivity : AppCompatActivity(), BaseViewWithData {

    protected open val mProgress: LoadingView? = null
    protected open var mError: ErrorView? = null
    protected open var mMessage: MessageView? = null

    private var lastToast: Toast? = null
    private var lastState: State<*>? = null

    protected val loadingViewTag = "Progress-${this::class.java.simpleName}"
    protected val errorViewTag = "Error-${this::class.java.simpleName}"
    protected val infoViewTag = "Info-${this::class.java.simpleName}"

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            mProgress?.hideProgress()
            mError?.hideError()
            mMessage?.hideMessage()
        }
    }

    override fun <T> handleDataState(state: State<T>) {
        when (state) {
            Loading -> onLoading()
            is Error -> onError(state.message.orEmpty())
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

    override fun hideKeyboard() { }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (lastToast != null)
            lastToast!!.show()
        if (lastState != null)
            handleDataState(lastState!!)
    }
}