/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 19:01
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentBasicBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.model.User

class BasicFragment : Fragment() {

    private lateinit var binding: FragmentBasicBinding
    private lateinit var onPageSelectedCallback: ViewPager2.OnPageChangeCallback

    var currentQuestion = 1
    var user = User(0, "sd", null, null, null, null, null)

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
                changePage(currentQuestion+1)
                binding.viewPagerBasic.setCurrentItem(currentQuestion, true)
            }
            else
                (requireActivity() as MainActivity).showDashboard() //should call navigation here
            if (currentQuestion == 4){
                binding.butNextQuest.text = getString(R.string.done)
            }
        }

        return binding.root
    }

    private fun changePage(toPosition: Int){
        if (toPosition > currentQuestion)
            when(toPosition){
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
            when(toPosition){
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

    override fun onDestroy() {
        binding.viewPagerBasic.unregisterOnPageChangeCallback(onPageSelectedCallback)
        super.onDestroy()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BasicFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}

interface IOnDataCollected {
    fun setData(key: String, value: Any)
}