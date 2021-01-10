/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 20:07
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentGenderBinding


class GenderFragment : Fragment() {
    private lateinit var binding: FragmentGenderBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(BasicQuizViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gender, container, false)

        binding.butWoman.setOnClickListener {
            (it as MaterialButton).setBackgroundColor(resources.getColor(R.color.primaryColour))
            it.setTextColor(resources.getColor(R.color.white))
            binding.butMan.setBackgroundColor(resources.getColor(R.color.textFieldColour))
            binding.butMan.setTextColor(resources.getColor(R.color.black))
            viewModel.update("gender", 1)
        }
        binding.butMan.setOnClickListener {
            (it as MaterialButton).setBackgroundColor(resources.getColor(R.color.primaryColour))
            it.setTextColor(resources.getColor(R.color.white))
            binding.butWoman.setBackgroundColor(resources.getColor(R.color.textFieldColour))
            binding.butWoman.setTextColor(resources.getColor(R.color.black))
            viewModel.update("gender", 0)
        }

        return  binding.root
    }

}