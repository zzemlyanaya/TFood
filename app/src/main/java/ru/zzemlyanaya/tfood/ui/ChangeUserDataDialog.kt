/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 22:27
 */

package ru.zzemlyanaya.tfood.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.VALUE
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.DialogUpdateUserDataBinding
import ru.zzemlyanaya.tfood.main.basicquiz.MEAS
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE


class ChangeUserDataDialog : DialogFragment(){

    private lateinit var binding: DialogUpdateUserDataBinding

    private var title = ""
    private var param = 0
    private var measurements = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_user_data, container, false)

        binding.textDialogTitle.text = title
        binding.textMeasures.text = measurements
        binding.inputEditParam.visibility = View.VISIBLE
        binding.textEditParam.setText(param.toString())
        binding.butMinus.setOnClickListener {
            param -= 1
            if (param < 0) param = 0
            binding.textEditParam.setText(param.toString())
        }
        binding.butPlus.setOnClickListener {
            param += 1
            binding.textEditParam.setText(param.toString())
        }
        binding.textEditParam.afterTextChanged { param = it.toIntOrNull() ?: 0 }
        binding.butSubmit.setOnClickListener {
            val intent = Intent()
            intent.putExtra(VALUE, param)
            intent.putExtra(TITLE, title)
            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
        binding.butCancel.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(TITLE)!!
            measurements = it.getString(MEAS)!!
            param = it.getInt(VALUE)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 72)
        dialog?.window?.setBackgroundDrawable(inset)
    }
}