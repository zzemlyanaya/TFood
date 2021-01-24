/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 12:51
 */

package ru.zzemlyanaya.tfood.main.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.ProductShort

class ProductSearchRecyclerAdapter (
        private val onCardClickListener: (String) -> Unit,
        private val onAddProductClickListener: (String) -> Unit,
        private var values: List<ProductShort>
        ) : RecyclerView.Adapter<ProductSearchRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textProductName.text = item.name
        holder.itemView.setOnClickListener { onCardClickListener(item._id) }
        holder.butAddProductToMeal.setOnClickListener { onAddProductClickListener(item._id) }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(new: List<ProductShort>) {
        (values as ArrayList).clear()
        (values as ArrayList).addAll(new)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textProductName: TextView = view.findViewById(R.id.itemProductName)
        val butAddProductToMeal: MaterialButton = view.findViewById(R.id.butAddProductToMeal)
    }
}