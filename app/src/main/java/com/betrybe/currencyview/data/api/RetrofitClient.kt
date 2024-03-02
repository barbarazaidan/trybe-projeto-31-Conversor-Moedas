package com.betrybe.currencyview.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val URL = "https://api.apilayer.com/exchangerates_data/"
    private val APIKEY = "gKSrABlHD03DgJxz5bn3CKCC0XK4gY01"

    val instance: ApiService by lazy {

        val apiInterceptor = InterceptorAPI(APIKEY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }
}