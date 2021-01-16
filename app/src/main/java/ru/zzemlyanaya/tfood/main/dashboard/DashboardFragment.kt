/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:08
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentDashboardBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.ui.circularprogressview.CPVSection


class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        with(binding.storiesRecyclerView){
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = StoriesRecyclerAdapter(
                { onStoryClick(it) },
                listOf(
                    "Почему важно питаться правильно",
                    "10 причин заняться йогой",
                    "Чем грозит нехватка кальция"
                )
            )
        }

        val prots = CPVSection(amount = 21f)
        binding.progressProts.apply {
            cap = 42f
            submitData(listOf(prots))
        }
        binding.textProts.text = "21/42"
        val fats = CPVSection(amount = 12f)
        binding.progressFats.apply {
            cap = 35f
            submitData(listOf(fats))
        }
        binding.textFats.text = "21/35"
        val carbs = CPVSection(amount = 32f)
        binding.progressCarbs.apply {
            cap = 83f
            submitData(listOf(carbs))
        }
        binding.textCarbs.text = "32/83"

        val animation: ObjectAnimator =
            ObjectAnimator.ofInt(binding.progressKcal, "progress", 0, 50)
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()
        animation.start()

        binding.butLogout.setOnClickListener {
            (requireActivity() as MainActivity).logout()
        }

        return binding.root
    }

    private fun onStoryClick(title: String) {
        // TODO show fragment w/ full story
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DashboardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}