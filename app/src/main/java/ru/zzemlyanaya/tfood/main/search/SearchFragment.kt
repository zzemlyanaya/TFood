/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:15
 */

package ru.zzemlyanaya.tfood.main.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zzemlyanaya.tfood.*
import ru.zzemlyanaya.tfood.databinding.FragmentSearchBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE
import ru.zzemlyanaya.tfood.model.Status
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    private var titleRes = 0
    private lateinit var whatToSearch: String
    private lateinit var binding: FragmentSearchBinding

    private var congrat = CongratsTypes.NONE

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            titleRes = it.getInt(TITLE)
            whatToSearch = it.getString(WHAT_TO_SEARCH).orEmpty()
        }
    }

    fun back() {
        if (congrat == CongratsTypes.NONE)
            (requireActivity() as MainActivity).showDairy()
        else
            (requireActivity() as MainActivity).showDashboard(congrat)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        binding.textSearchTitle.text = getString(titleRes)
        binding.butBackToDairy.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
        binding.butAddNewProduct.apply {
            text = when(whatToSearch) {
                "product" -> getString(R.string.add_new_product)
                else -> getString(R.string.add_new_activity)
            }
            setOnClickListener {
//                when(whatToSearch) {
//                    "product" -> (requireActivity() as MainActivity).showAddNewProduct()
//                    else -> (requireActivity() as MainActivity).showAddActivity()
//                }
            }
        }

        binding.productSearchRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchRecyclerAdapter(
                    { id -> showInfo(id) },
                    { id -> addToDay(id) },
                    ArrayList()
            )
        }

        binding.textSearch.afterTextChanged {
            viewModel.search(it, whatToSearch).observe(viewLifecycleOwner, { res ->
                res?.let {
                    when(res.status) {
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            Log.d(DEBUG_TAG, res.message.orEmpty())
                        }
                        Status.SUCCESS -> {
                            (binding.productSearchRecycler.adapter as SearchRecyclerAdapter).updateData(res.data!!)
                        }
                    }
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.congratsLiveData.observe(viewLifecycleOwner, {
            congrat = it
        })
    }

    private fun showInfo(id: String){
        (requireActivity() as MainActivity).showInfo(id, whatToSearch, titleRes)
    }

    private fun addToDay(id: String) {
        when(whatToSearch) {
            "product" -> viewModel
                .addFood(
                    id,
                    requireContext().getStringByLocale(titleRes, Locale.getDefault())
                        .replaceFirstChar { it.lowercase(Locale.getDefault()) },
                    100f
                )
            else -> {
                viewModel
                    .addActivity(
                        requireContext().getStringByLocale(titleRes, Locale.getDefault())
                            .replaceFirstChar { it.lowercase(Locale.getDefault()) },
                        60f, id
                    )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: Int, whatToSearch: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(TITLE, title)
                    putString(WHAT_TO_SEARCH, whatToSearch)
                }
            }
    }
}