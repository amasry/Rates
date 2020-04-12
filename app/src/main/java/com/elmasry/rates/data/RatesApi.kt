package com.elmasry.rates.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("/api/android/latest")
    fun getRates(@Query("base") baseCurrency: String) : Single<RatesResponse>
}