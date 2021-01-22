/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 20:24
 */

package ru.zzemlyanaya.tfood.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.MEAL
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentProductSearchBinding
import ru.zzemlyanaya.tfood.main.MainActivity

class ProductSearchFragment : Fragment() {

    private lateinit var meal: String
    private lateinit var binding: FragmentProductSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            meal = it.getString(MEAL, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_search, container, false)

        binding.textMeal.text = meal
        binding.butBackToDairy.setOnClickListener {
            (requireActivity() as MainActivity).showDairy()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(meal: String) =
            ProductSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL, meal)
                }
            }
    }
}