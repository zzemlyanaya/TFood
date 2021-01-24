/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 14:22
 */

package ru.zzemlyanaya.tfood.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.ShortView

class SearchViewModel: ViewModel() {
    private val remoteRepository = RemoteRepository()

    private val searchResults = MutableLiveData<List<ShortView>>(emptyList())

    fun search(search: String, whatToSearch: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = when(whatToSearch) {
                "product" -> remoteRepository.searchProduct(search)
//                    Result(
//                            data = listOf(ShortView("0", "Яблоко"), ShortView("1", "Морковь")),
//                            error = null)
                "sport" -> remoteRepository.searchSport(search)
//                    Result(
//                            data = listOf(ShortView("0", "Бег в медленном темпе"), ShortView("1", "Скандинавская ходьба")),
//                            error = null)
                else -> remoteRepository.searchHousework(search)
//                    Result(
//                            data = listOf(ShortView("0", "Мытьё полов"), ShortView("1", "Мытьё посуды")),
//                            error = null)
            }
            if (result.error == null) {
                searchResults.postValue(result.data)
                emit(Resource.success(data = result.data))
            }
            else
                emit(Resource.error(data = null, message = result.error))
        }
        catch (e: Exception){
            e.printStackTrace()
            emit(Resource.error(data = null, message = "Ooops, try again."))
        }
    }

    fun getData() = searchResults.value


}