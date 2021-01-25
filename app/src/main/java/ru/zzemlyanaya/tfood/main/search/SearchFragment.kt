/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 19:16
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
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.WHAT_TO_SEARCH
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentSearchBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.TITLE
import ru.zzemlyanaya.tfood.model.Status

class SearchFragment : Fragment() {

    private var title_res = 0
    private lateinit var whatToSearch: String
    private lateinit var binding: FragmentSearchBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title_res = it.getInt(TITLE)
            whatToSearch = it.getString(WHAT_TO_SEARCH).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        binding.textSearchTitle.text = getString(title_res)
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
        (requireActivity() as MainActivity).showInfo(id, whatToSearch, title_res)
    }

    private fun addToDay(id: String) {
        val token = LocalRepository.getInstance().getPref(PrefsConst.FIELD_USER_TOKEN) as String
        val date = LocalRepository.getInstance().getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String
        when(whatToSearch) {
            "product" -> viewModel.addFood(token, id, whatToSearch, date, 100f)
            else -> {
                viewModel.addActivity(token, date, whatToSearch, 60f, id)
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