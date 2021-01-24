/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 14:22
 */

package ru.zzemlyanaya.tfood.main.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.model.Resource

class InfoViewModel : ViewModel() {
    private val remoteRepository = RemoteRepository()

    fun getProduct(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = remoteRepository.getProductInfo(id)
            if (result.error == null) {
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