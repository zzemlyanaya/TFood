/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 12:25
 */

package ru.zzemlyanaya.tfood.main.dairy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.Record

class RecordRecyclerAdapter (
        //private val onCardClickListener: () -> Unit,
        private var values: List<Record>
) : RecyclerView.Adapter<RecordRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dairy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textRecordTitle.text = item.name
        holder.textRecordKcal.text = item.kcal.toString()
        holder.textRecordNote.text = item.note
        //holder.itemView.setOnClickListener { onCardClickListener() }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(new: List<Record>) {
        (values as ArrayList).clear()
        (values as ArrayList).addAll(new)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textRecordTitle: TextView = view.findViewById(R.id.textRecordTitle)
        val textRecordKcal: TextView = view.findViewById(R.id.textRecordKcal)
        val textRecordNote: TextView = view.findViewById(R.id.textRecordNote)
    }
}