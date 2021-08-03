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
import ru.zzemlyanaya.core.databinding.DialogMessageBinding
import ru.zzemlyanaya.core.model.MessageEntity
import ru.zzemlyanaya.core.presentation.MessageView

class InfoDialog(private val message: MessageEntity) : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogMessageBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_message, null, false)
        binding.message = message
        binding.butCloseMessage.setOnClickListener { dismiss() }

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

        fun view(fm: FragmentManager, tag: String, message: MessageEntity): MessageView {
            return object : MessageView {

                private var dialog: InfoDialog? = null

                override fun showMessage() {
                    if (dialog != null) return
                    dialog = InfoDialog(message).also { it.show(fm, tag) }
                }

                override fun hideMessage() {
                    val dialog = fm.findFragmentByTag(tag) as InfoDialog? ?: dialog
                    dialog?.dismissAllowingStateLoss()
                    this.dialog = null
                }
            }
        }

        fun view(fragment: Fragment, tag: String, message: MessageEntity): MessageView {
            return view(fragment.requireFragmentManager(), tag, message)
        }
    }
}