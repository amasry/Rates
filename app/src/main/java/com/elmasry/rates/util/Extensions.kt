package com.elmasry.rates.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> AppCompatActivity.observe(
    liveData: LiveData<T>,
    action: (T) -> Unit
) {
    liveData.observe(this, Observer(action))
}

val <T> T.exhaustive: T
    get() = this