package com.elmasry.rates.mapper

import com.elmasry.rates.data.RatesResponse
import com.elmasry.rates.model.Rates
import com.elmasry.rates.util.Mapper

class RatesMapper() : Mapper<RatesResponse, Rates> {

    override fun map(input: RatesResponse): Rates {
        return Rates(input.baseCurrency, input.rates)
    }
}