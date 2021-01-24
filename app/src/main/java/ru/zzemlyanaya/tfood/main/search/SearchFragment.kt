/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 13:57
 */

package ru.zzemlyanaya.tfood.main.search

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
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.WHAT_TO_SEARCH
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentSearchBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE
import ru.zzemlyanaya.tfood.model.Status

class SearchFragment : Fragment() {

    private lateinit var title: String
    private lateinit var whatToSearch: String
    private lateinit var binding: FragmentSearchBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)!!
            whatToSearch = it.getString(WHAT_TO_SEARCH)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        binding.textSearchTitle.text = title
        binding.butBackToDairy.setOnClickListener {
            (requireActivity() as MainActivity).showDairy()
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
                            Toast.makeText(requireContext(), getString(R.string.computing), Toast.LENGTH_SHORT).show()
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

    private fun showInfo(id: String){
//        when (whatToSearch){
//            "product" -> (requireActivity() as MainActivity).showProductInfo(id)
//            else -> (requireActivity() as MainActivity).showActivityInfo(id)
//        }
    }

    private fun addToDay(id: String) {
        //TODO send add/product view viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String?, whatToSearch: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(WHAT_TO_SEARCH, whatToSearch)
                }
            }
    }
}