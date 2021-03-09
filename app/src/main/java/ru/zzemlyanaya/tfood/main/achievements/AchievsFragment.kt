/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.03.2021, 18:14
 */

package ru.zzemlyanaya.tfood.main.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentAchievsBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.model.Achievement

class AchievsFragment : Fragment() {

    private lateinit var binding: FragmentAchievsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievs, container, false)

        binding.butBackFromAchievs.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        with(binding.achievsRecyclerView){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AchievsRecyclerAdapter(
                listOf(
                    Achievement("1", R.drawable.ic_water_achiev, getString(R.string.water_title), "Описание будет позже", 0.2f, 1),
                    Achievement("2", R.drawable.ic_nutrients_achiev, getString(R.string.nutrients), "Описание будет позже", 0.3f, 1),
                    Achievement("3", R.drawable.ic_sleep_achiev, getString(R.string.sleep_title), "Описание будет позже", 0.55f, 1)
                )
            )
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AchievsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}