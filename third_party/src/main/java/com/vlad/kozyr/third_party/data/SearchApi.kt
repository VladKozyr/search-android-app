package com.vlad.kozyr.third_party.data

import com.vlad.kozyr.third_party.data.model.SearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchApi {
    @GET("api/Search/WebSearchAPI")
    fun searchForQuery(
        @Query("q") q: String,
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int = 10
    ): Single<SearchResponse>

    @GET("api/spelling/AutoComplete")
    fun getAutoCompleteForQuery(
        @Query("text") q: String
    ): Single<List<String>>
}