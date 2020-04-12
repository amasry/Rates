package com.elmasry.rates.util

import android.content.Context
import android.graphics.drawable.Drawable
import java.text.DecimalFormat
import java.util.Currency
import java.util.Locale

@OpenForTest
class CurrencyUtil {

    private val decimalFormat = DecimalFormat("#.##")

    val decimalSeparator: Char
        get() {
            return decimalFormat.decimalFormatSymbols.decimalSeparator
        }

    fun getCurrencyName(currencyCode: String): String {
        return Currency.getInstance(currencyCode).getDisplayName(Locale.getDefault())
    }

    fun getCountryFlag(context: Context, currencyCode: String): Drawable? {
        val resId = context.resources.getIdentifier(
            "ic_flag_${currencyCode.toLowerCase(Locale.getDefault())}",
            "drawable",
            context.packageName
        )
        if (resId == 0) {
            return null
        }
        return context.getDrawable(resId)
    }

    fun formatCurrencyValue(currencyValue: Double): String {
        return decimalFormat.format(currencyValue)
    }

    fun parseValue(value: String): Double {
        if (value == decimalSeparator.toString()) {
            return 0.0
        }
        return decimalFormat.parse(value)?.toDouble() ?: 0.0
    }
}