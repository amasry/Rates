package com.elmasry.rates.data

import com.elmasry.rates.mapper.RatesMapper
import com.elmasry.rates.model.Rates
import com.google.common.truth.Truth
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RatesRepositoryImplTest {

    @Mock
    lateinit var ratesApi: RatesApi

    @Test
    fun getRates() {
        val ratesRepository = RatesRepositoryImpl(ratesApi, RatesMapper()) // we can use the real mapper here
        val ratesResponse = RatesResponse(
            baseCurrency = "EUR",
            rates = mapOf("USD" to 1.1, "CAD" to 3.3)
        )
        val rates = Rates(
            baseCurrency = "EUR",
            conversionRates = mapOf("USD" to 1.1, "CAD" to 3.3)
        )

        given(ratesApi.getRates("EUR")).willReturn(Single.just(ratesResponse))

        val result = ratesRepository.getRates("EUR").blockingGet()
        Truth.assertThat(result).isEqualTo(rates)
    }
}