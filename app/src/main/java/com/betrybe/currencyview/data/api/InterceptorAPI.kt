package com.betrybe.currencyview.data.api

import okhttp3.Interceptor
import okhttp3.Response

class InterceptorAPI(private val keyAPI: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().header("apikey", keyAPI).build()
        return chain.proceed(newRequest)
    }
}