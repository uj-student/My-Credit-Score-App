package com.example.mycreditscore.model.repo.network

import android.util.Log
import com.example.mycreditscore.BuildConfig
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeUnit

object NetworkClient {
    private var apiClient: Retrofit? = null

    fun getInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getClient(interceptor: OkHttpClient): Retrofit? {
        if (apiClient == null) {
            apiClient = Retrofit.Builder()
                .client(interceptor)
                .baseUrl(BuildConfig.CREDIT_SCORE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            initRxErrorHandler()
        }
        return apiClient
    }

    private fun initRxErrorHandler() { //As per RxJava2 official documentation.
        RxJavaPlugins.setErrorHandler { error ->
            var throwable = error
            if (error is UndeliverableException) {
                throwable = throwable.cause
            }
            if (throwable is IOException || throwable is SocketException) {
                // irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (throwable is InterruptedException) {
                // some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (throwable is NullPointerException || throwable is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), throwable)
                return@setErrorHandler
            }
            if (throwable is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), throwable)
                return@setErrorHandler
            }
            Log.w("Undeliverable exception", throwable)
        }
    }
}