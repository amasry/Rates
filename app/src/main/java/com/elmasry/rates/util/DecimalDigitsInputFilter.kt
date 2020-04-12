package com.elmasry.rates.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern

class DecimalDigitsInputFilter(
    digitsBeforeSeparator: Int,
    digitsAfterSeparator: Int,
    private val decimalSeparator: Char
) :
    InputFilter {
    private val mPattern =
        Pattern.compile("[0-9]{0,${digitsBeforeSeparator - 1}}+((\\$decimalSeparator[0-9]{0,${digitsAfterSeparator - 1}})?)||(\\.)?")

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (dest.toString().contains(decimalSeparator) && source?.toString() == decimalSeparator.toString()) {
            return ""
        }
        val matcher: Matcher = mPattern.matcher(dest.toString())
        return if (!matcher.matches()) "" else null
    }
}