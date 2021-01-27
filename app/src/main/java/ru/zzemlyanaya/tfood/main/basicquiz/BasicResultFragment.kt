/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 12:17
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.*
import ru.zzemlyanaya.tfood.databinding.FragmentBasicResultBinding
import ru.zzemlyanaya.tfood.main.MainActivity

class BasicResultFragment : Fragment() {
    private var weightVal = 0
    private var weight = 0
    private var border = 0f
    private var kcalNorm = 0
    private var waterNorm = 0

    private lateinit var binding: FragmentBasicResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weightVal = it.getInt(WEIGHT_VAL)
            weight = it.getInt(WEIGHT)
            border = it.getFloat(BORDER_WEIGHT)
            kcalNorm = it.getInt(KCAL_NORM)
            waterNorm = it.getInt(WATER_NORM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_result, container, false)

        var max = 0
        when(weightVal){
            -1 -> {
                binding.textWeightVal.text = getString(R.string.weight_val_less)
                max = (border.toInt()+5)*2
            }
            0 -> {
                binding.textWeightVal.text = getString(R.string.weight_val_norm)
                max = weight*2
            }
            else -> {
                binding.textWeightVal.text = getString(R.string.weight_val_more)
                max = (border.toInt()-5)*2
            }
        }

        binding.weightValProgress.max = max
        val animation: ObjectAnimator =
                ObjectAnimator.ofInt(binding.weightValProgress, "progress", 0, weight)
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()
        animation.start()

        binding.textKcalIdeal.text = kcalNorm.toString()
        binding.textWaterIdeal.text = waterNorm.toString()

        binding.butAlright.setOnClickListener { (requireActivity() as MainActivity).showDashboard() }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(weightVal: Int, border: Float, weight: Int, kcalNorm: Int, waterNorm: Int) =
                BasicResultFragment().apply {
                    arguments = Bundle().apply {
                        putInt(WEIGHT_VAL, weightVal)
                        putInt(WEIGHT, weight)
                        putFloat(BORDER_WEIGHT, border)
                        putInt(KCAL_NORM, kcalNorm)
                        putInt(WATER_NORM, waterNorm)
                    }
                }
    }
}