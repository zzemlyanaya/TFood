/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.03.2021, 18:14
 */

package ru.zzemlyanaya.tfood.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentAccountSettingsBinding
import ru.zzemlyanaya.tfood.main.MainActivity

class AccountSettingsFragment : Fragment() {
    private lateinit var binding: FragmentAccountSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_settings, container, false)

        binding.butBackToBaseSettings.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }

        return binding.root
    }

}