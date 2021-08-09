/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.fragment

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.core.dialog.ErrorDialog
import ru.zzemlyanaya.core.dialog.InfoDialog
import ru.zzemlyanaya.core.model.MessageEntity
import ru.zzemlyanaya.core.network.model.*
import ru.zzemlyanaya.core.presentation.BaseViewWithData
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.LoadingView
import ru.zzemlyanaya.core.presentation.MessageView

abstract class CoreFragment : Fragment(), BaseViewWithData {

    protected open val mProgress: LoadingView? = null
    protected open var mError: ErrorView? = null
    protected open var mMessage: MessageView? = null

    private var lastToast: Toast? = null

    protected val loadingViewTag = "Progress-${this::class.java.simpleName}"
    protected val errorViewTag = "Error-${this::class.java.simpleName}"
    protected val infoViewTag = "Info-${this::class.java.simpleName}"

    override fun <T> handleDataState(state: State<T>) {
        when (state) {
            Loading -> onLoading()
            is Error<T> -> onError(state.message)
            Empty -> onEmpty()
            is Success<T> -> onData(state.data)
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
            .view(parentFragmentManager, errorViewTag, message)
            .also { it.showError() }
    }

    override fun <T> onData(data: T) {
        mProgress?.hideProgress()
    }

    fun showMessage(message: MessageEntity) {
        mMessage = InfoDialog
            .view(parentFragmentManager, infoViewTag, message)
            .also { it.showMessage() }
    }

    fun hideMessage() {
        mMessage?.hideMessage()
    }

    fun showToast(@StringRes resId: Int) {
        cancelToast()
        lastToast = Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).apply { show() }
    }

    fun showToast(text: String) {
        cancelToast()
        lastToast = Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).apply { show() }
    }

    private fun cancelToast() {
        lastToast?.cancel()
        lastToast = null
    }

    override fun hideKeyboard() {
        activity?.let { activity ->
            val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // check if no view has focus:
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        mError = null
        mMessage = null
    }
}