/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 13:19
 */

package ru.zzemlyanaya.tfood.main.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.*
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentInfoBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE
import ru.zzemlyanaya.tfood.model.Activities
import ru.zzemlyanaya.tfood.model.Product
import ru.zzemlyanaya.tfood.model.Status
import java.util.*


class InfoFragment : Fragment() {
    private var product: Product? = null
    private var activities: Activities? = null
    private var id = ""
    private var whatToShow = ""
    private var title = 0

    val weight = LocalRepository.getInstance().getPref(PrefsConst.FIELD_USER_DATA).toString()
        .split(";")[3].toInt()

    private val localRepository by lazy { LocalRepository.getInstance() }

    private val viewModel by lazy { ViewModelProviders.of(this).get(InfoViewModel::class.java) }
    private lateinit var binding: FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID).orEmpty()
            whatToShow = it.getString(WHAT_TO_SEARCH).orEmpty()
            title = it.getInt(TITLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)

        when (whatToShow){
            "product" -> {
                binding.inputPortions.visibility = View.VISIBLE
                binding.inputStandartValue.apply {
                    (layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentWidth = 0.7f
                    suffixText = getString(R.string.gram_short)
                }
                binding.scrollNutrients.visibility = View.VISIBLE
            }
            else -> {
                binding.inputPortions.visibility = View.INVISIBLE
                binding.inputStandartValue.apply {
                    (layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentWidth = 0.88f
                    suffixText = getString(R.string.min_short)
                }
                binding.scrollNutrients.visibility = View.INVISIBLE
            }
        }

        binding.butBackProduct.setOnClickListener { back() }
        binding.butAddSth.setOnClickListener {
            val len = binding.textStandartValue.text.toString().toFloat()*
                    (binding.textPortions.text.toString().toFloatOrNull() ?: 1f)
            addToDay(len)
            back()
        }

        binding.textStandartValue.afterTextChanged {
            val len = it.toFloatOrNull() ?: 0f
            val port = binding.textPortions.text.toString().toFloatOrNull() ?: 1f
            when(whatToShow) {
                "product" ->  {
                    binding.textProductKcal.text = "%.1f".format(len/100 * (product?.kkal ?: 0f) * port)
                    binding.textProductProts.text = "%.1f".format(len/100 * (product?.prots ?: 0f) * port)
                    binding.textProductFats.text = "%.1f".format(len/100 * (product?.fats ?: 0f) * port)
                    binding.textProductCarbs.text = "%.1f".format(len/100 * (product?.carbs ?: 0f) * port)
                    binding.textProductFiber.text = "%.1f".format(len/100 * (product?.alimentaryFiber ?: 0f) * port)
                }
                else -> binding.textProductKcal.text = (len/60 * activities!!.ecost * weight).toString()
            }
        }

        binding.textPortions.afterTextChanged {
            val len = binding.textStandartValue.text.toString().toFloatOrNull() ?: 0f
            val port = it.toFloatOrNull() ?: 1f
            when(whatToShow) {
                "product" -> {
                    binding.textProductKcal.text = "%.1f".format(len/100 * (product?.kkal ?: 0f) * port)
                    binding.textProductProts.text = "%.1f".format(len/100 * (product?.prots ?: 0f) * port)
                    binding.textProductFats.text = "%.1f".format(len/100 * (product?.fats ?: 0f) * port)
                    binding.textProductCarbs.text = "%.1f".format(len/100 * (product?.carbs ?: 0f) * port)
                    binding.textProductFiber.text = "%.1f".format(len/100 * (product?.alimentaryFiber ?: 0f) * port)
                }
                else -> binding.textProductKcal.text = (len/60 * activities!!.ecost * weight).toString()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInfo()
    }

    private fun getInfo() {
        viewModel.getSth(id, whatToShow).observe(viewLifecycleOwner, {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.textProductName.visibility = View.INVISIBLE
                        binding.progressInfo.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Log.d(DEBUG_TAG, it.message.toString())
                    }
                    Status.SUCCESS -> {
                        binding.progressInfo.visibility = View.INVISIBLE
                        binding.textProductName.visibility = View.VISIBLE
                        if (whatToShow == "product") {
                            product = it.data as Product
                            binding.textProductName.text = product!!.name
                            binding.textStandartValue.setText("100")
                            binding.textProductKcal.text = product!!.kkal.toString()
                            binding.textProductCarbs.text = product!!.carbs.toString()
                            binding.textProductFats.text = product!!.fats.toString()
                            binding.textProductProts.text = product!!.prots.toString()
                            binding.textProductFiber.text = product!!.alimentaryFiber.toString()
                            binding.textProductVitamins.text = product!!.vitamins.toString()
                            binding.textProductMinerals.text = product!!.minerals.toString()
                        }
                        else {
                            activities = it.data as Activities
                            binding.textProductName.text = activities!!.name
                            binding.textStandartValue.setText("60")
                            binding.textProductKcal.text = (activities!!.ecost * weight).toString()
                        }
                    }
                }
            }
        })
    }

    private fun addToDay(length: Float){
        when(whatToShow) {
            "product" -> {
                viewModel.addFood(id,
                        requireContext().getStringByLocale(title, Locale.ENGLISH).decapitalize(),
                        length)
            }
            else -> {
                viewModel.addActivity(
                        requireContext().getStringByLocale(title, Locale.ENGLISH).decapitalize(),
                    length, activities!!._id)
            }
        }
    }

    fun back(){
        (requireActivity() as MainActivity).showAddSth(title, whatToShow)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String, whatToShow: String, title: Int) =
                InfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ID, id)
                        putString(WHAT_TO_SEARCH, whatToShow)
                        putInt(TITLE, title)
                    }
                }
    }
}