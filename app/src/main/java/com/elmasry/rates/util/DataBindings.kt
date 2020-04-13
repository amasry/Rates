package com.elmasry.rates.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

@BindingAdapter("requestFocus")
fun requestFocus(et: EditText, requestFocus: Boolean) {
    if (requestFocus) {
        et.isFocusableInTouchMode = true
        et.requestFocus()
        val imm: InputMethodManager? = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
        et.setSelection(et.text.length);
    }
}