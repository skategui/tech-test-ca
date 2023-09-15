package com.android.banks.di

import com.android.banks.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// deps to inject related to the network requests
val networkModule = module {

    fun provideRetrofitInstance(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG)
                    level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()


        val builder = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }


    single { provideRetrofitInstance(baseUrl =  BuildConfig.API_BASE_URL)}
}