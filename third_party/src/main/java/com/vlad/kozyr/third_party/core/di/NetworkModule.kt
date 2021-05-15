package com.vlad.kozyr.third_party.core.di

import com.vlad.kozyr.third_party.core.network.HeaderInterceptor
import com.vlad.kozyr.third_party.data.SearchApi
import com.vlad.kozyr.third_party.data.SearchRepository
import com.vlad.kozyr.third_party.domain.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val headers = mapOf(
            "X-RapidAPI-Key" to "1d2c2e7acamsh3afdb778d391909p18b199jsnecede6d6ddee",
            "useQueryString" to "true",
            "x-rapidapi-host" to "contextualwebsearch-websearch-v1.p.rapidapi.com"
        )
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(HeaderInterceptor(headers))
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
            .build()
    }

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://contextualwebsearch-websearch-v1.p.rapidapi.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    fun provideSearchRepository(searchApi: SearchApi): SearchRepository {
        return SearchRepositoryImpl(searchApi)
    }
}