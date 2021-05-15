package com.vlad.kozyr.third_party.presentation

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.vlad.kozyr.third_party.core.DisposableViewModel
import com.vlad.kozyr.third_party.data.SearchRepository
import com.vlad.kozyr.third_party.data.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val searchRepository: SearchRepository
) : DisposableViewModel(), LifecycleObserver {

    private val inputProcessor: PublishProcessor<String> = PublishProcessor.create()
    private var disposable: Disposable? = null

    private val _autoComplete = MutableLiveData<List<String>>()
    val autoComplete: LiveData<List<String>>
        get() = _autoComplete

    val searchData: PublishSubject<PagingData<SearchResult>> = PublishSubject.create()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun prepare() {
        inputProcessor.subscribeOn(Schedulers.io())
            .flatMap { query: String -> searchRepository.searchForQuery(query) }
            .subscribe(
                { result -> searchData.onNext(result) },
                { error ->
                    // TODO add error handling
                }
            )
            .autoClear()
    }

    fun updateAutoComplete(query: String) {
        disposable.cancel()
        disposable = searchRepository.getSearchSuggestion(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value -> _autoComplete.postValue(value) },
                { error -> Log.i("TAG", "updateAutoComplete: $error") })

    }

    fun onSearchClicked(input: String) {
        inputProcessor.onNext(input)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.cancel()
    }

    private fun Disposable?.cancel() {
        if (this?.isDisposed == false) {
            dispose()
        }
    }
}