/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:16
 */

package ru.zzemlyanaya.core.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.zzemlyanaya.core.R
import ru.zzemlyanaya.core.databinding.DialogErrorBinding
import ru.zzemlyanaya.core.presentation.ErrorView

class ErrorDialog(private val error: String?) : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogErrorBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_error, null, false)
        binding.error = error.orEmpty()
        binding.butCloseError.setOnClickListener { dismiss() }

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
            .apply {
                val back = ColorDrawable(Color.TRANSPARENT)
                val inset = InsetDrawable(back, 2)
                this.window?.setBackgroundDrawable(inset)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }

    companion object {

        fun view(fm: FragmentManager, tag: String, error: String?): ErrorView {
            return object : ErrorView {

                private var dialog: ErrorDialog? = null

                override fun showError() {
                    if (dialog != null) return
                    dialog = ErrorDialog(error).also { it.show(fm, tag) }
                }

                override fun hideError() {
                    val dialog = fm.findFragmentByTag(tag) as ErrorDialog? ?: dialog
                    dialog?.dismissAllowingStateLoss()
                    this.dialog = null
                }
            }
        }

        fun view(fragment: Fragment, tag: String, error: String?): ErrorView {
            return view(fragment.requireFragmentManager(), tag, error)
        }
    }
}