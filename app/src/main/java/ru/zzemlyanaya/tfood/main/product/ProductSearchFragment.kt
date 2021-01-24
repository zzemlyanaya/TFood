/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 12:51
 */

package ru.zzemlyanaya.tfood.main.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.MEAL
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentProductSearchBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.model.Status

class ProductSearchFragment : Fragment() {

    private lateinit var meal: String
    private lateinit var binding: FragmentProductSearchBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(ProductViewModel::class.java) }

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
        binding.butAddNewProduct.setOnClickListener {
            (requireActivity() as MainActivity).showAddNewProduct()
        }

        binding.productSearchRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ProductSearchRecyclerAdapter(
                    { id -> showProductInfo(id) },
                    { id -> addProductToMeal(id) },
                    ArrayList()
            )
        }

        binding.textSearchProduct.afterTextChanged {
            viewModel.search(it).observe(viewLifecycleOwner, { res ->
                res?.let {
                    when(res.status) {
                        Status.LOADING -> {
                            Toast.makeText(requireContext(), getString(R.string.computing), Toast.LENGTH_SHORT).show()
                        }
                        Status.ERROR -> {
                            Log.d(DEBUG_TAG, res.message.orEmpty())
                        }
                        Status.SUCCESS -> {
                            (binding.productSearchRecycler.adapter as ProductSearchRecyclerAdapter).updateData(res.data!!)
                        }
                    }
                }
            })
        }

        return binding.root
    }

    private fun showProductInfo(id: String){
        (requireActivity() as MainActivity).showProductInfo(id)
    }

    private fun addProductToMeal(id: String) {
        //TODO send add/product view viewModel
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