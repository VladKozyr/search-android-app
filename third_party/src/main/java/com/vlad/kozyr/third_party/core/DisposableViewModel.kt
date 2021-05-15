package com.vlad.kozyr.third_party.core

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class DisposableViewModel : ViewModel() {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    protected fun Disposable.autoClear() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
