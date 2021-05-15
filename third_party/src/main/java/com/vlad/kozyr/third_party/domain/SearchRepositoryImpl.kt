package com.vlad.kozyr.third_party.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.vlad.kozyr.third_party.data.SearchApi
import com.vlad.kozyr.third_party.data.SearchRepository
import com.vlad.kozyr.third_party.data.model.SearchResult
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ActivityScoped
class SearchRepositoryImpl
@Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {

    override fun getSearchSuggestion(query: String): Single<List<String>> {
        return searchApi.getAutoCompleteForQuery(query)
    }

    @ExperimentalCoroutinesApi
    override fun searchForQuery(query: String): Flowable<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { SearchResultPagingSource(searchApi, query) }
        ).flowable
    }
}