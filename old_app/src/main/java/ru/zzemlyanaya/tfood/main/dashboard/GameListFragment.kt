/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentGameListBinding
import ru.zzemlyanaya.tfood.main.MainActivity


class GameListFragment : Fragment() {
    private lateinit var binding: FragmentGameListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_list, container, false)

        with(binding.gameRecyclerView.recycler) {
            layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
//                .apply {
//                spanSizeLookup = object : SpanSizeLookup() {
//                    override fun getSpanSize(position: Int): Int {
//                        return when (adapter!!.getItemViewType(position)) {
//                            MenuAdapter.ITEM -> 1
//                            MenuAdapter.FULLSIZE -> 2
//                            else -> 1
//                        }
//                    }
//                }
//                recyclerView.setLayoutManager(mLayoutManager)
//            }
            adapter = GamesRecyclerAdapter(
                { },
                listOf(
                    "Packman",
                    "Змейка"
                )
            )
        }

        binding.butBackFromGameList.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }

        return binding.root
    }


}