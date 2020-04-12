package com.elmasry.rates.ui

import com.elmasry.rates.model.Amount

interface OnCurrencyRateChangedListener {
    fun onCurrencyRateChanged(amount: Amount)
}