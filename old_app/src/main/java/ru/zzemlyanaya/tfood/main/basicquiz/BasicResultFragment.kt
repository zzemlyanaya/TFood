/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 12:25
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
import ru.zzemlyanaya.tfood.model.WeightGrade
import ru.zzemlyanaya.tfood.model.WeightResult

class BasicResultFragment : Fragment() {
    private var weight = 0
    private var weightResult = WeightResult(0f, WeightGrade.PERFECT)
    private var kcalNorm = 0
    private var waterNorm = 0

    private lateinit var binding: FragmentBasicResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_basic_result, container, false)

        binding.butAlright.setOnClickListener {
            (requireActivity() as MainActivity).showSleepQuiz()
        }

        return binding.root
    }

    private fun setupData() {
        var max = 0
        when (weightResult.weightValue) {
            WeightGrade.TOO_LITTLE -> {
                binding.textWeightVal.text = getString(R.string.weight_val_less)
                max = (weightResult.recommendedWeight.toInt() + 5) * 2
            }
            WeightGrade.PERFECT -> {
                binding.textWeightVal.text = getString(R.string.weight_val_norm)
                max = weightResult.recommendedWeight.toInt() * 2
            }
            WeightGrade.TOO_MUCH -> {
                binding.textWeightVal.text = getString(R.string.weight_val_more)
                max = (weightResult.recommendedWeight.toInt() - 5) * 2
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
    }
}