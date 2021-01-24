/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 14:16
 */

package ru.zzemlyanaya.tfood.main.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.PRODUCT_ID
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.main.search.SearchViewModel
import ru.zzemlyanaya.tfood.model.Product


class ProductFragment : Fragment() {
    private lateinit var product: Product
    private var id = ""

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(PRODUCT_ID).orEmpty()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
                ProductFragment().apply {
                    arguments = Bundle().apply {
                        putString(PRODUCT_ID, id)
                    }
                }
    }
}