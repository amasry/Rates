package com.elmasry.rates.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface RatesApi {

    @GET("/v5/2b188b71a2f03ba62f63ad2f/latest/{baseCurrency}")
    fun getRates(@Path("baseCurrency") baseCurrency: String): Single<RatesResponse>
}