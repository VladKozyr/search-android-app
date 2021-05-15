package com.vlad.kozyr.third_party.data

import androidx.paging.PagingData
import com.vlad.kozyr.third_party.data.model.SearchResult
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single


interface SearchRepository {
    fun getSearchSuggestion(query: String): Single<List<String>>

    fun searchForQuery(query: String): Flowable<PagingData<SearchResult>>
}