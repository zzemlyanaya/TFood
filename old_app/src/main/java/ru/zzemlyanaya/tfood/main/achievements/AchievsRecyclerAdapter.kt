/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:16
 */

package ru.zzemlyanaya.tfood.main.achievements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.willy.ratingbar.ScaleRatingBar
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.Achievement

class AchievsRecyclerAdapter(
    private var values: List<Achievement>
) : RecyclerView.Adapter<AchievsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achiev, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.icon.setImageResource(item.icon_res)
        holder.name.text = item.name
        holder.description.text = item.description
        holder.progress.max = 1
        holder.progress.setProgress(item.progress.toInt(), true)
        holder.medals.rating = item.level.toFloat()
        holder.medals.setIsIndicator(true)
        holder.medals.isClickable = false
        holder.medals.isScrollable = false

    }

    fun update(new: List<Achievement>) {
        if (values.isNotEmpty())
            (values as ArrayList).clear()
        values = values.plus(new)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.textAchievTitle)
        val description: TextView = view.findViewById(R.id.textAchievDescription)
        val progress: LinearProgressIndicator = view.findViewById(R.id.progressAchiev)
        val medals: ScaleRatingBar = view.findViewById(R.id.achievMedals)
        val icon: ImageView = view.findViewById(R.id.iconAchiev)
    }
}