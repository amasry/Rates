package com.elmasry.rates

import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T

fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}