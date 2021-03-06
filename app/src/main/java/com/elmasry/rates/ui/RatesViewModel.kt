package com.elmasry.rates.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmasry.rates.R
import com.elmasry.rates.data.RatesRepository
import com.elmasry.rates.model.Amount
import com.elmasry.rates.model.Rates
import com.elmasry.rates.ui.RatesViewModel.ViewAction.ShowError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

private val DEFAULT_BASE_CURRENCY = Amount("EUR", 100.0)

class RatesViewModel(
    private val ratesRepository: RatesRepository
) : ViewModel(), OnCurrencyRateChangedListener, LifecycleObserver {

    private lateinit var rates: Rates
    val data = MutableLiveData<List<Amount>>()
    val viewAction = MutableLiveData<ViewAction>()
    private var disposable: Disposable? = null
    private var timerDisposable: Disposable? = null
    private var baseCurrency = DEFAULT_BASE_CURRENCY

    init {
        loadCurrencyRates()
    }

    private fun loadCurrencyRates() {
        disposable?.dispose()
        disposable = ratesRepository
            .getRates(baseCurrency.currencyCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    rates = it
                    createDataList()
                },
                onError = {
                    viewAction.postValue(ShowError(R.string.general_error))
                }
            )
    }

    private fun createDataList() {
        val list = listOf(baseCurrency) + rates.conversionRates
            .toList()
            .filter { it.first != rates.baseCurrency }
            .sortedBy {
                // retain the same list order
                data.value?.indexOfFirst { oldAmount ->
                    it.first == oldAmount.currencyCode
                }
            }
            .map { rate -> Amount(rate.first, rate.second * baseCurrency.value) }
        data.postValue(list)
    }

    override fun onCleared() {
        disposable?.dispose()
        timerDisposable?.dispose()
        super.onCleared()
    }

    override fun onCurrencyRateChanged(amount: Amount) {
        if (baseCurrency != amount) {
            if (baseCurrency.currencyCode == amount.currencyCode) {
                baseCurrency = amount
                createDataList()
            } else {
                baseCurrency = amount
                loadCurrencyRates()
            }
        }
    }

    sealed class ViewAction {
        data class ShowError(val errorResId: Int) : ViewAction()
    }
}