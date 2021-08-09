/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import ru.zzemlyanaya.core.network.model.Error
import ru.zzemlyanaya.core.network.model.Loading
import ru.zzemlyanaya.core.network.model.Success
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentBasicBinding
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.main.MainActivity
import toothpick.ktp.KTP

class BasicFragment : Fragment() {

    private lateinit var binding: FragmentBasicBinding
    private lateinit var onPageSelectedCallback: ViewPager2.OnPageChangeCallback
    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(BasicQuizViewModel::class.java)
    }

    private var currentQuestion = 1


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        KTP.closeScope(Scopes.APP_SCOPE)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewPagerBasic.adapter = QuizAdapter(requireActivity() as AppCompatActivity, 6)
        onPageSelectedCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changePage(position)
                super.onPageSelected(position)
            }
        }
        binding.viewPagerBasic.registerOnPageChangeCallback(onPageSelectedCallback)

        binding.butNextQuest.setOnClickListener {
            if (currentQuestion != 5) {
                changePage(currentQuestion + 1)
                binding.viewPagerBasic.setCurrentItem(currentQuestion, true)
            } else {
                if (viewModel.isDataValid()) {
                    sendData()
                } else
                    showWarningDialog()
            }
            if (currentQuestion == 4) {
                binding.butNextQuest.text = getString(R.string.done)
            }
        }

        return binding.root
    }

    private fun changePage(toPosition: Int) {
        if (toPosition > currentQuestion)
            when (toPosition) {
                1 -> {
                    binding.step2.setIconTintResource(R.color.primaryColour)
                }
                2 -> binding.step3.setIconTintResource(R.color.primaryColour)
                3 -> binding.step4.setIconTintResource(R.color.primaryColour)
                4 -> binding.step5.setIconTintResource(R.color.primaryColour)
                5 -> {
                    binding.step6.setIconTintResource(R.color.primaryColour)
                    binding.butNextQuest.text = getString(R.string.done)
                }
            }
        else
            when (toPosition) {
                0 -> binding.step2.setIconTintResource(R.color.textFieldColour)
                1 -> binding.step3.setIconTintResource(R.color.textFieldColour)
                2 -> binding.step4.setIconTintResource(R.color.textFieldColour)
                3 -> binding.step5.setIconTintResource(R.color.textFieldColour)
                4 -> {
                    binding.step6.setIconTintResource(R.color.textFieldColour)
                    binding.butNextQuest.text = getString(R.string.next)
                }
            }
        currentQuestion = toPosition
    }

    private fun showWarningDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setTitle(getString(R.string.hmm))
            .setMessage(getString(R.string.basic_quiz_not_complete))
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun sendData() {
        viewModel.sendData().observe(viewLifecycleOwner, {
            when(it) {
                Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                }
                is Error<*> -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                is Success<*> -> {
                    (requireActivity() as MainActivity).showBasicResult()
                }
            }
        })
    }

    override fun onDestroy() {
        binding.viewPagerBasic.unregisterOnPageChangeCallback(onPageSelectedCallback)
        super.onDestroy()
    }
}
