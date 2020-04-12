package com.elmasry.rates.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.elmasry.rates.model.Amount
import com.elmasry.rates.util.CurrencyUtil

class RatesItemViewModel(
    private val context: Context,
    private val currencyUtil: CurrencyUtil,
    private val onCurrencyRateChangedListener: OnCurrencyRateChangedListener
) {
    private lateinit var amount: Amount
    val value = ObservableField<String>()
    var currencyCode = ObservableField<String>()
    val currencyName = ObservableField<String>()
    val flagDrawable = ObservableField<Drawable?>()

    fun bind(amount: Amount) {
        this.amount = amount
        currencyCode.set(amount.currencyCode)
        currencyName.set(currencyUtil.getCurrencyName(amount.currencyCode))
        value.set(currencyUtil.formatCurrencyValue(amount.value))
        flagDrawable.set(currencyUtil.getCountryFlag(context, amount.currencyCode))
    }

    fun onValueChanged(newValue: String) {
        if (newValue.isNotEmpty() && value.get() != newValue && !newValue.endsWith(currencyUtil.decimalSeparator)) {
            amount = Amount(amount.currencyCode, currencyUtil.parseValue(newValue))
            onCurrencyRateChangedListener.onCurrencyRateChanged(amount)
            value.set(newValue)
        }
    }

    fun onValueFocused(isFocused: Boolean) {
        if (isFocused) {
            onCurrencyRateChangedListener.onCurrencyRateChanged(amount)
        }
    }
}