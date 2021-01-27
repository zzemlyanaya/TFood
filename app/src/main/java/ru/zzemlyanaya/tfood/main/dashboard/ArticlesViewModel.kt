/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 13:39
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.model.Article
import ru.zzemlyanaya.tfood.model.Result

class ArticlesViewModel : ViewModel() {
    private val remoteRepository = RemoteRepository()

    val articleList = MutableLiveData(emptyList<Article>())
    val article = MutableLiveData(Article())

    init {
        getAllArticles()
    }

    fun getAllArticles() = CoroutineScope(Dispatchers.IO).launch {
        val res: Result<List<Article>> = remoteRepository.getAllArticles()
        if (res.error == null)
            articleList.postValue(res.data)
        else
            Log.d(DEBUG_TAG, res.error)
    }

    fun getArticle(id: String) =  CoroutineScope(Dispatchers.IO).launch {
        val res: Result<Article> = remoteRepository.getArticleByID(id)
        if (res.error == null)
            article.postValue(res.data)
        else
            Log.d(DEBUG_TAG, res.error)
    }

}