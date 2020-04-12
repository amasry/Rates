package com.elmasry.rates.data

import com.elmasry.rates.model.Rates
import com.elmasry.rates.util.Mapper
import io.reactivex.Single

interface RatesRepository {
    fun getRates(baseCurrency: String): Single<Rates>
}

class RatesRepositoryImpl(
    private val ratesApi: RatesApi,
    private val ratesMapper: Mapper<RatesResponse, Rates>
) : RatesRepository {

    override fun getRates(baseCurrency: String): Single<Rates> {
        return ratesApi
            .getRates(baseCurrency)
            .map(ratesMapper::map)
    }
}