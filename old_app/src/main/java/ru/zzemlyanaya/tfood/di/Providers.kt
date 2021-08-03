/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import com.kryptoprefs.preferences.KryptoBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HeaderMap
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.Prefs
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.IServerApi
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.data.remote.SynchronousCallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class ServerApiProvider @Inject constructor(gson: Gson) : Provider<IServerApi> {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(IServerApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
        .build()

    override fun get(): IServerApi = retrofit.create(IServerApi::class.java)
}

class RemoteRepositoryProvider @Inject constructor(
    private val api: IServerApi,
    private val gson: Gson,
    @field:Named("headers")
    private val headers: Map<String, String>
) :
    Provider<RemoteRepository> {
    override fun get(): RemoteRepository = RemoteRepository(api, gson, headers)
}

class LocalRepositoryProvider @Inject constructor(private val prefs: Prefs) :
    Provider<LocalRepository> {
    override fun get(): LocalRepository = LocalRepository(prefs)
}


class CiceroneProvider @Inject constructor(private val router: Router) :
    Provider<Cicerone<Router>> {
    override fun get(): Cicerone<Router> = Cicerone.create(router)
}

class NavigatorProvider @Inject constructor(private val cicerone: Cicerone<Router>) :
    Provider<NavigatorHolder> {
    override fun get(): NavigatorHolder = cicerone.getNavigatorHolder()
}

class PrefsProvider @Inject constructor(private val context: Context) : Provider<Prefs> {
    override fun get(): Prefs =
        Prefs(
            KryptoBuilder.hybrid(
                context,
                PrefsConst.PREFS_NAME
            )
        )
}
