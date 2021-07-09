/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.main.search

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.ShortView

class SearchRecyclerAdapter (
        private val onCardClickListener: (String) -> Unit,
        private val onAddProductClickListener: (String) -> Unit,
        private var values: List<ShortView>
        ) : RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textProductName.text = item.name
        holder.itemView.setOnClickListener { onCardClickListener(item._id) }
        holder.butAddProductToMeal.setOnClickListener {
            onAddProductClickListener(item._id)
            holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7D0A"))
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7D0A"))
            holder.textProductName.setTextColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(new: List<ShortView>) {
        (values as ArrayList).clear()
        (values as ArrayList).addAll(new)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textProductName: TextView = view.findViewById(R.id.itemProductName)
        val butAddProductToMeal: AppCompatImageButton = view.findViewById(R.id.butAddProductToMeal)
    }
}