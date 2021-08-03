/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:16
 */

package ru.zzemlyanaya.core.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.zzemlyanaya.core.R
import ru.zzemlyanaya.core.presentation.LoadingView

class LoadingDialog : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setView(R.layout.dialog_progress)
            .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }

    companion object {

        fun view(fm: FragmentManager, tag: String): LoadingView {
            return object : LoadingView {

                private var dialog: LoadingDialog? = null

                override fun showProgress() {
                    if (dialog != null) return
                    dialog = LoadingDialog().also { it.show(fm, tag) }
                }

                override fun hideProgress() {
                    val dialog = fm.findFragmentByTag(tag) as LoadingDialog? ?: dialog
                    dialog?.dismissAllowingStateLoss()
                    this.dialog = null
                }
            }
        }

        fun view(fragment: Fragment, tag: String): LoadingView {
            return view(fragment.requireFragmentManager(), tag)
        }
    }
}