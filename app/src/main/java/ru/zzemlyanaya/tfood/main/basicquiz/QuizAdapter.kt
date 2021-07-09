/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.zzemlyanaya.tfood.R

class QuizAdapter(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    private val quests = listOf(
        listOf(R.string.height_with_emoji, R.string.sm, R.string.how_to_height),
        listOf(R.string.weight_with_emoji, R.string.kg, R.string.how_to_weight),
        listOf(R.string.breast_diametr, R.string.sm, R.string.how_to_breast)
    )

    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int): Fragment {
        val pos2 = position%3
        return when(position) {
            0 -> NameFragment()
            1 -> GenderFragment()
            2 -> AgeFragment()
            else -> GenericFragment.newInstance(quests[pos2][0], quests[pos2][1], quests[pos2][2])
        }
    }

}