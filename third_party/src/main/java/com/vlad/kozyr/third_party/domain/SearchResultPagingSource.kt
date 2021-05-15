package com.vlad.kozyr.third_party.domain

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.vlad.kozyr.third_party.data.SearchApi
import com.vlad.kozyr.third_party.data.model.SearchResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchResultPagingSource(
    private val searchApi: SearchApi,
    private val query: String
) : RxPagingSource<Int, SearchResult>() {

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, SearchResult>> {
        if (query.isEmpty()) {
            return Single.just(LoadResult.Page(emptyList(), prevKey = null, nextKey = null))
        }
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return Single.create { emitter ->
            searchApi.searchForQuery(query, page, pageSize)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    emitter.onSuccess(
                        LoadResult.Page(
                            data = it.value,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (it.totalCount / pageSize <= page) null else page + 1
                        )
                    )
                }, { emitter.onSuccess(LoadResult.Error(it)) })
        }
    }
}