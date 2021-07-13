/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentBaseSettingsBinding
import ru.zzemlyanaya.tfood.main.MainActivity

class BaseSettingsFragment : Fragment() {

    private lateinit var binding: FragmentBaseSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base_settings, container, false)

        binding.butAccount.setOnClickListener { (requireActivity() as MainActivity).showAccountSettings() }
        binding.butAboutApp.setOnClickListener { }
        binding.butNotifications.setOnClickListener { }
        binding.butBack.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.butLogout.setOnClickListener { (requireActivity() as MainActivity).logout() }

        return binding.root
    }

}