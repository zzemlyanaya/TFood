/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 11:40
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentNameBinding

class NameFragment : Fragment() {

    private lateinit var binding: FragmentNameBinding
    private val viewModel by lazy { ViewModelProviders.of(requireActivity()).get(BasicQuizViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name, container, false)

        binding.textName.afterTextChanged {
            when {
                it.isEmpty() -> binding.inputName.error = getString(R.string.empty_name)
                else -> {
                    binding.inputName.error = null
                    viewModel.update("username", it)
                }
            }
        }

        return binding.root
    }
}