/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 19.01.2021, 13:07
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R

class StoriesRecyclerAdapter(
        private val onCardClickListener: (String) -> Unit,
        private var values: List<String>
        ) : RecyclerView.Adapter<StoriesRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_articles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textStoryTitle.text = item
        holder.itemView.setOnClickListener { onCardClickListener(item) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textStoryTitle: TextView = view.findViewById(R.id.textStoryTitle)

    }
}