package com.elmasry.rates.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class RatesResponse(
    @Json(name = "baseCurrency") val baseCurrency: String,
    @Json(name = "rates") val rates : Map<String, Double>
)