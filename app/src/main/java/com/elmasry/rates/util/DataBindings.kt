package com.elmasry.rates.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: T?) {
    val adapter = recyclerView.adapter
    if (adapter is BindableAdapter<*> && data != null) {
        @Suppress("UNCHECKED_CAST")
        (adapter as BindableAdapter<T>).setData(data)
    }
}

interface BindableAdapter<T> {
    fun setData(data: T)
}