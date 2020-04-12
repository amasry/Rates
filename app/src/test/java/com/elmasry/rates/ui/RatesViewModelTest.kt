package com.elmasry.rates.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elmasry.rates.R
import com.elmasry.rates.data.RatesRepository
import com.elmasry.rates.model.Amount
import com.elmasry.rates.model.Rates
import com.elmasry.rates.ui.RatesViewModel.ViewAction.ShowError
import com.google.common.truth.Truth.assertThat
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class RatesViewModelTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var ratesRepository: RatesRepository

    private lateinit var viewModel: RatesViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    private val rates = Rates(
        baseCurrency = "EUR",
        conversionRates = mapOf(
            "USD" to 1.69,
            "AUD" to 2.56
        )
    )

    private val amountsList = listOf(
        Amount("EUR", 100.0),
        Amount("USD", 169.0),
        Amount("AUD", 256.0)
    )

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `viewModel should load rates from repository with default base currency`() {
        given(ratesRepository.getRates("EUR")).willReturn(Single.just(rates))

        viewModel = RatesViewModel(ratesRepository)

        assertThat(viewModel.data.value).isEqualTo(amountsList)
    }

    @Test
    fun `onResume should start a timer to load rates every 1 second`() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        given(ratesRepository.getRates("EUR")).willReturn(Single.just(rates))

        viewModel = RatesViewModel(ratesRepository)
        viewModel.onResume()
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        then(ratesRepository).should(times(4)).getRates("EUR")
        assertThat(viewModel.data.value).isEqualTo(amountsList)
    }

    @Test
    fun `onCurrencyChanged should update base currency and rates`() {
        val updatesRates = Rates(
            baseCurrency = "USD",
            conversionRates = mapOf(
                "AUD" to 2.0,
                "EUR" to 1.8
            )
        )

        // list order should be retained
        val updatedAmountList = listOf(
            Amount("USD", 100.0),
            Amount("EUR", 180.0),
            Amount("AUD", 200.0)
        )
        given(ratesRepository.getRates("EUR")).willReturn(Single.just(rates))
        given(ratesRepository.getRates("USD")).willReturn(Single.just(updatesRates))

        viewModel = RatesViewModel(ratesRepository)
        viewModel.onCurrencyRateChanged(Amount("USD", 100.0))

        assertThat(viewModel.data.value).isEqualTo(updatedAmountList)
    }

    @Test
    fun `onCurrencyChanged with same base currency should update data list`() {
        val updatedAmountList = listOf(
            Amount("EUR", 200.0),
            Amount("USD", 338.0),
            Amount("AUD", 512.0)
        )

        given(ratesRepository.getRates("EUR")).willReturn(Single.just(rates))

        viewModel = RatesViewModel(ratesRepository)
        viewModel.onCurrencyRateChanged(Amount("EUR", 200.0))

        assertThat(viewModel.data.value).isEqualTo(updatedAmountList)
    }

    @Test
    fun `onPause should stop timer`() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        given(ratesRepository.getRates("EUR")).willReturn(Single.just(rates))

        viewModel = RatesViewModel(ratesRepository)
        viewModel.onResume()
        viewModel.onPause()
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        then(ratesRepository).should(times(1)).getRates("EUR")
        assertThat(viewModel.data.value).isEqualTo(amountsList)
    }

    @Test
    fun `onError should post general error message`() {
        given(ratesRepository.getRates("EUR")).willReturn(Single.error(IOException()))

        viewModel = RatesViewModel(ratesRepository)

        assertThat(viewModel.viewAction.value).isEqualTo(ShowError(R.string.general_error))
    }
}