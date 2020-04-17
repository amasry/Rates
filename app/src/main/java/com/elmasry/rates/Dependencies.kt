package com.elmasry.rates

import com.elmasry.rates.data.RatesApi
import com.elmasry.rates.data.RatesRepository
import com.elmasry.rates.data.RatesRepositoryImpl
import com.elmasry.rates.mapper.RatesMapper
import com.elmasry.rates.ui.RatesViewModel
import com.elmasry.rates.util.CurrencyUtil
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val BASE_URL = "https://prime.exchangerate-api.com/"

object Dependencies {

    fun get() = module {
        single {
            createRetrofit()
        }

        single<RatesRepository> {
            RatesRepositoryImpl(
                ratesApi = get<Retrofit>().create(RatesApi::class.java),
                ratesMapper = RatesMapper()
            )
        }

        single {
            CurrencyUtil()
        }

        viewModel {
            RatesViewModel(ratesRepository = get())
        }
    }

    private fun createMoshi(): Moshi = Moshi.Builder().build()

    private fun createRetrofit(
        baseUrl: String = BASE_URL,
        client: OkHttpClient = createOkHttpClient(),
        moshi: Moshi = createMoshi()
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            ).addConverterFactory(
                ScalarsConverterFactory.create()
            )
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()) // <-- default subscribeOn()
            )
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }

    private fun createOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}