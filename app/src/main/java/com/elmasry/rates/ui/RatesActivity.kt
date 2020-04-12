package com.elmasry.rates.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmasry.rates.BR
import com.elmasry.rates.R
import com.elmasry.rates.databinding.ActivityRatesBinding
import com.elmasry.rates.ui.RatesViewModel.ViewAction.ShowError
import com.elmasry.rates.util.CurrencyUtil
import com.elmasry.rates.util.exhaustive
import com.elmasry.rates.util.observe
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatesBinding
    private val viewModel: RatesViewModel by viewModel()
    private val currencyUtil: CurrencyUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rates)
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)
        lifecycle.addObserver(viewModel)
        with(binding.rvRates) {
            layoutManager = LinearLayoutManager(this@RatesActivity)
            isFocusable = false
            adapter = RatesRecyclerAdapter(currencyUtil, viewModel)
        }
        observe(viewModel.viewAction) {
            when (it) {
                is ShowError -> Toast.makeText(this, it.errorResId, Toast.LENGTH_LONG).show()
            }.exhaustive
        }
    }
}
