package com.elmasry.rates.util

interface Mapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
}
