package com.elmasry.rates.ui

import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elmasry.rates.databinding.ItemRateBinding
import com.elmasry.rates.model.Amount
import com.elmasry.rates.ui.RatesRecyclerAdapter.RatesItemViewHolder
import com.elmasry.rates.util.BindableAdapter
import com.elmasry.rates.util.CurrencyUtil
import com.elmasry.rates.util.DecimalDigitsInputFilter

class RatesRecyclerAdapter(
    private val currencyUtil: CurrencyUtil,
    private val onRateChangedListener: OnCurrencyRateChangedListener
) :
    ListAdapter<Amount, RatesItemViewHolder>(RatesDiffCallback()),
    BindableAdapter<List<Amount>> {

    init {
        setHasStableIds(true)
    }


    override fun setData(data: List<Amount>) {
        submitList(data)
    }

    inner class RatesItemViewHolder(
        private val itemBinding: ItemRateBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(amount: Amount) {
            itemBinding.vm?.bind(amount)
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesItemViewHolder {
        val itemBinding =
            ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        with(itemBinding) {
            vm = RatesItemViewModel(parent.context, currencyUtil, onRateChangedListener)
            etRate.keyListener =
                DigitsKeyListener.getInstance("0123456789${currencyUtil.decimalSeparator}");
            etRate.filters = arrayOf(DecimalDigitsInputFilter(9, 2, currencyUtil.decimalSeparator))
        }
        return RatesItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RatesItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).currencyCode.hashCode().toLong()
    }
}

class RatesDiffCallback : DiffUtil.ItemCallback<Amount>() {
    override fun areItemsTheSame(p0: Amount, p1: Amount) = p0.currencyCode == p1.currencyCode

    override fun areContentsTheSame(p0: Amount, p1: Amount) = p0 == p1

    override fun getChangePayload(oldItem: Amount, newItem: Amount): Any? {
        return newItem
    }
}
