/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 1:04
 */

package ru.zzemlyanaya.tfood.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.joda.time.DateTime
import org.joda.time.Months
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentProfileBinding
import ru.zzemlyanaya.tfood.main.MainActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val localRepository = LocalRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        setUpUserData(localRepository.getPref(PrefsConst.FIELD_USER_DATA).toString().split(';'))

        binding.butSettings.setOnClickListener { (requireActivity() as MainActivity).showBaseSettings() }

        return binding.root
    }

    private fun setUpUserData(data: List<String>){
        val date = data[1].split('-')
        val birthdate = DateTime(date[0].toInt(), date[1].toInt(), date[2].toInt(), 0, 0, 0)
        val now = DateTime()
        var age: Months = Months.monthsBetween(birthdate, now)
        val years = age.dividedBy(12)
        age = age.minus(years.multipliedBy(12))
        binding.apply {
            textUsername.text = data[0]
            textUserHeight.text = data[2]
            textUserWeight.text = data[3]
            textUserAge.text = "${years.months} ${getString(R.string.years_short)} ${age.months} ${getString(R.string.month_short)}"
        }
    }

}