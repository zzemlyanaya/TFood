/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.04.2021, 13:31
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentArticleListBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.model.Article

class ArticleListFragment : Fragment() {
    private lateinit var binding: FragmentArticleListBinding

    private val articlesViewModel by lazy { ViewModelProviders.of(requireActivity()).get(ArticlesViewModel::class.java) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articlesViewModel.articleList.observe(viewLifecycleOwner, { list ->
            (binding.articleRecyclerView.recycler.adapter as ArticleRecyclerAdapter).update(list)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false)

        with(binding.articleRecyclerView.recycler){
            layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            adapter = ArticleRecyclerAdapter(
                { onArticleClick(it) },
                emptyList()
            )
        }

        binding.butBackFromArticleList.setOnClickListener{
            (requireActivity() as MainActivity).onBackPressed()
        }

        return binding.root
    }

    private fun onArticleClick(id: Article) {
        articlesViewModel.getArticle(id._id)
        (requireActivity() as MainActivity).showArticle(id, "dashboard")
    }
}