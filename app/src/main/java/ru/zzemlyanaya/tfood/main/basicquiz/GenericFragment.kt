/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 20:23
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentGenericBinding

const val TITLE = "title"
const val MEAS = "measurements"
const val HOW_TO = "how to"

class GenericFragment : Fragment() {
    private lateinit var binding: FragmentGenericBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(BasicQuizViewModel::class.java)}

    private lateinit var title: String
    private lateinit var measurements: String
    private lateinit var howTo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = getString(it.getInt(TITLE))
            measurements = getString(it.getInt(MEAS))
            howTo = getString(it.getInt(HOW_TO))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_generic, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.textGenericTitle.text = title
        binding.textGenericMeasures.text = measurements
        binding.textGenericHowTo.text = howTo

        when(title){
            getString(R.string.height) -> {
                binding.textGeneric.afterTextChanged {
                    if (it.toInt() < 10 || it.toInt() > 220)
                        binding.inputGeneric.error = getString(R.string.generic_error)
                    else {
                        viewModel.update("height", it.toInt())
                        binding.inputGeneric.error = null
                    }
                }
            }
            getString(R.string.weight) -> {
                binding.textGeneric.afterTextChanged {
                    if (it.toInt() < 10 || it.toInt() > 200)
                        binding.inputGeneric.error = getString(R.string.generic_error)
                    else {
                        viewModel.update("weight", it.toInt())
                        binding.inputGeneric.error = null
                    }
                }
            }
            getString(R.string.breast_diametr) -> {
                binding.textGeneric.afterTextChanged {
                    if (it.toInt() < 10 || it.toInt() > 150)
                        binding.inputGeneric.error = getString(R.string.generic_error)
                    else {
                        viewModel.update("gk", it.toInt())
                        binding.inputGeneric.error = null
                    }
                }
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(stringTitle: Int, stringMeas: Int, stringHowTo: Int) =
            GenericFragment().apply {
                arguments = Bundle().apply {
                    putInt(TITLE, stringTitle)
                    putInt(MEAS, stringMeas)
                    putInt(HOW_TO, stringHowTo)
                }
            }
    }
}