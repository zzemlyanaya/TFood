/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.Article

class ArticleRecyclerAdapter(
    private val onCardClickListener: (Article) -> Unit,
    private var values: List<Article>
) : RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textArticleTitle.text = item.title
        holder.itemView.setOnClickListener { onCardClickListener(item) }
    }

    fun update(new: List<Article>) {
        if (values.isNotEmpty())
            (values as ArrayList).clear()
        values = values.plus(new)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textArticleTitle: TextView = view.findViewById(R.id.itemArticleTitle)

    }
}