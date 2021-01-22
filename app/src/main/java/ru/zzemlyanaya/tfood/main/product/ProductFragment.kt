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
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.PRODUCT
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.Product


class ProductFragment : Fragment() {
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(PRODUCT) as Product
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(product: Product) =
                ProductFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(PRODUCT, product)
                    }
                }
    }
}