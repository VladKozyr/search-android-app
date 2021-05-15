package com.vlad.kozyr.third_party.core.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        headers.forEach { builder.addHeader(it.key, it.value) }
        return chain.proceed(builder.build())
    }
}