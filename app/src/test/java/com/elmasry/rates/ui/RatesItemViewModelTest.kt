package com.elmasry.rates.ui

import android.content.Context
import android.graphics.drawable.Drawable
import com.elmasry.rates.any
import com.elmasry.rates.model.Amount
import com.elmasry.rates.util.CurrencyUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RatesItemViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var currencyUtil: CurrencyUtil

    @Mock
    lateinit var onCurrencyRateChangedListener: OnCurrencyRateChangedListener

    @Mock
    lateinit var drawable: Drawable

    lateinit var viewModel: RatesItemViewModel

    private lateinit var amount: Amount

    @Before
    fun setUp() {
        viewModel = RatesItemViewModel(context, currencyUtil, onCurrencyRateChangedListener)
        amount = Amount("EUR", 100.0)
        given(currencyUtil.getCurrencyName(amount.currencyCode)).willReturn("Euro")
        given(currencyUtil.formatCurrencyValue(amount.value)).willReturn("100")
        given(currencyUtil.getCountryFlag(context, amount.currencyCode)).willReturn(drawable)
    }

    @Test
    fun `bind should post correct values to observables`() {
        viewModel.bind(amount)

        assertThat(viewModel.currencyCode.get()).isEqualTo(amount.currencyCode)
        assertThat(viewModel.currencyName.get()).isEqualTo("Euro")
        assertThat(viewModel.value.get()).isEqualTo("100")
        assertThat(viewModel.flagDrawable.get()).isEqualTo(drawable)
    }

    @Test
    fun `onValueChanged should call listener`() {
        given(currencyUtil.parseValue("200,0")).willReturn(200.0)

        viewModel.bind(amount)
        viewModel.onValueChanged("200,0")

        assertThat(viewModel.value.get()).isEqualTo("200,0")
        then(onCurrencyRateChangedListener).should()
            .onCurrencyRateChanged(Amount(amount.currencyCode, 200.0))
    }

    @Test
    fun `onValueChanged with empty value should do nothing`() {
        viewModel.onValueChanged("")

        assertThat(viewModel.value.get()).isNull()
        then(onCurrencyRateChangedListener).should(never()).onCurrencyRateChanged(any())
    }

    @Test
    fun `onValueChanged with the same value should do nothing`() {
        viewModel.bind(amount)
        viewModel.onValueChanged("100")

        assertThat(viewModel.value.get()).isEqualTo("100")
        then(onCurrencyRateChangedListener).should(never()).onCurrencyRateChanged(any())
    }

    @Test
    fun `onValueChanged with decimal separator should do nothing`() {
        given(currencyUtil.decimalSeparator).willReturn(',')

        viewModel.onValueChanged("100,")

        assertThat(viewModel.value.get()).isNull()
        then(onCurrencyRateChangedListener).should(never()).onCurrencyRateChanged(any())
    }


    @Test
    fun `onValueFocused should call OnCurrencyRateChangedListener`() {
        viewModel.bind(amount)
        viewModel.onValueFocused(true)

        then(onCurrencyRateChangedListener).should().onCurrencyRateChanged(amount)
    }

    @Test
    fun `onValueFocused with false should not call OnCurrencyRateChangedListener`() {
        viewModel.bind(amount)
        viewModel.onValueFocused(false)

        then(onCurrencyRateChangedListener).should(never()).onCurrencyRateChanged(any())
    }
}
