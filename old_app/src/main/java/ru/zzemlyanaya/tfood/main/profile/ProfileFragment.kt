/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.joda.time.DateTime
import org.joda.time.Months
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.VALUE
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentProfileBinding
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.MainViewModel
import ru.zzemlyanaya.tfood.main.basicquiz.MEAS
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE
import ru.zzemlyanaya.tfood.uikit.ChangeUserDataDialog
import toothpick.ktp.KTP
import javax.inject.Inject
import javax.inject.Named


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    @field:Named("token")
    lateinit var token: String
    private var today = ""

    private val requestValue = 1

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
        super.onCreate(savedInstanceState)
        today = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        setUpUserData(localRepository.getPref(PrefsConst.FIELD_USER_DATA).toString().split(';'))

        binding.butSettings.setOnClickListener { (requireActivity() as MainActivity).showBaseSettings() }
        binding.butAchievements.setOnClickListener {
            (requireActivity() as MainActivity).showAchievements(
                "profile"
            )
        }

        return binding.root
    }

    private fun setUpUserData(data: List<String>) {
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
            textUserAge.text =
                "${years.months} ${getString(R.string.years_short)} ${age.months} ${getString(R.string.month_short)}"
        }
        binding.textUserHeight.setOnClickListener {
            onParamChange(getString(R.string.height), data[2].toInt(), getString(R.string.sm))
        }
        binding.textUserWeight.setOnClickListener {
            onParamChange(getString(R.string.weight), data[3].toInt(), getString(R.string.kg))
        }
    }

    private fun onParamChange(title: String, value: Int, meas: String) {
        val dialog = ChangeUserDataDialog()
        dialog.setTargetFragment(this, requestValue)
        dialog.arguments = Bundle().apply {
            putString(TITLE, title)
            putString(MEAS, meas)
            putInt(VALUE, value)
        }
        dialog.show(parentFragmentManager, this.javaClass.name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestValue) {
                val newValue = data?.getIntExtra(VALUE, 0)!!
                val title = data.getStringExtra(TITLE)!!
                update(title, newValue.toString())
            }
        }
    }

    private fun update(title: String, new: String) {
        val list = localRepository.getPref(PrefsConst.FIELD_USER_DATA).toString()
            .split(';') as ArrayList<String>
        when (title) {
            getString(R.string.height) -> {
                mainViewModel.updateUserHeight(new.toInt())
                binding.textUserHeight.text = new
                list[2] = new
            }
            getString(R.string.weight) -> {
                mainViewModel.updateUserWeight(new.toInt())
                binding.textUserWeight.text = new
                list[3] = new
            }
        }
        localRepository.updatePref(PrefsConst.FIELD_USER_DATA, list.joinToString(";"))
    }
}