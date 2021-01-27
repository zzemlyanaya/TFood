/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 13:39
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.ID
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentArticleBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.model.Article

class ArticleFragment : Fragment() {
    private lateinit var id: String
    private lateinit var binding: FragmentArticleBinding

    private val viewModel by lazy { ViewModelProviders.of(requireActivity()).get(ArticlesViewModel::class.java) }

    private var article: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = it.getSerializable(ID) as Article
        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel.article.observe(viewLifecycleOwner, {
//            article = it
//            updateUI()
//        })
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)

        binding.butBackFromArticle.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.textArticleTitle.text = article?.title
        binding.textArticleBody.text = article?.body

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(article: Article) =
                ArticleFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ID, article)
                    }
                }
    }
}