package com.frogobox.frogonewsapi.data.source

import android.content.Context
import com.frogobox.frogonewsapi.util.NewsUrl
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Faisal Amir
 * FrogoBox Inc License
 * =========================================
 * consumable-code-news-api
 * Copyright (C) 15/03/2020.
 * All rights reserved
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * LinkedIn : linkedin.com/in/faisalamircs
 * -----------------------------------------
 * FrogoBox Software Industries
 * com.frogobox.frogonewsapi.data.source
 *
 */
interface NewsApiService {

    companion object Factory {

        private var isUsingChuckInterceptor = false
        private lateinit var context: Context

        fun usingChuckInterceptor(context: Context){
            isUsingChuckInterceptor = true
            this.context = context
        }

        val getApiService: NewsApiService by lazy {
            val mLoggingInterceptor = HttpLoggingInterceptor()
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val mClient = if (isUsingChuckInterceptor) {
                OkHttpClient.Builder()
                    .addInterceptor(mLoggingInterceptor)
                    .addInterceptor(ChuckInterceptor(context))
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            }

            val mRetrofit = Retrofit.Builder()
                .baseUrl(NewsUrl.NEWS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build()

            mRetrofit.create(NewsApiService::class.java)
        }
    }

}