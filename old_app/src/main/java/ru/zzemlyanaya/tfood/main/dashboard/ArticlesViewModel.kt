/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
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
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.model.Article
import ru.zzemlyanaya.tfood.model.Result
import toothpick.ktp.KTP
import javax.inject.Inject

class ArticlesViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

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

    fun getArticle(id: String) = CoroutineScope(Dispatchers.IO).launch {
        val res: Result<Article> = remoteRepository.getArticleByID(id)
        if (res.error == null)
            article.postValue(res.data)
        else
            Log.d(DEBUG_TAG, res.error)
    }

}