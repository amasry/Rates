package com.elmasry.rates.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class RatesResponse(
    @Json(name = "base") val baseCurrency: String,
    @Json(name = "conversion_rates") val rates : Map<String, Double>
)