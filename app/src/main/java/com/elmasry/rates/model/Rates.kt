package com.elmasry.rates.model

data class Rates(
    val baseCurrency: String,
    val conversionRates: Map<String, Double>
)
