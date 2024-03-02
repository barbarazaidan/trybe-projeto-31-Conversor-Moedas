package com.betrybe.currencyview.data.api

import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    // precisa da palavra "suspend" para usar com corrotinas
    @GET("symbols")
    suspend fun getCurrentSymbols(): Response<CurrencySymbolResponse>
}