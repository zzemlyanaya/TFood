/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 12:59
 */

package ru.zzemlyanaya.tfood.main.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.model.Product
import ru.zzemlyanaya.tfood.model.ProductShort
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result

class ProductViewModel: ViewModel() {
    private val remoteRepository = RemoteRepository()

    private val searchResults = MutableLiveData<List<ProductShort>>(emptyList())
    private var chosenProduct: Product? = null

    fun search(search: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            //val result = remoteRepository.searchProduct(search)
            val result = Result(
                    data = listOf(ProductShort("0", "Яблоко"), ProductShort("1", "Морковь")),
                    error = null)
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

    fun getProduct(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = remoteRepository.getProductInfo(id)
            if (result.error == null) {
                withContext(Dispatchers.Main) { chosenProduct = result.data }
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
}