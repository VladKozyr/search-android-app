package com.vlad.kozyr.third_party.presentation

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.paging.LoadState
import com.vlad.kozyr.third_party.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: SearchViewModel by viewModels()
    private val adapter by lazy { SearchResultAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            searchRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                footer = SearchLoadStateAdapter { adapter.retry() },
                header = SearchLoadStateAdapter { adapter.retry() }
            )

            searchButton.setOnClickListener {
                viewModel.onSearchClicked(autoCompleteTextView.text.toString())
            }
        }
        viewModel.searchData.subscribe(
            { value -> adapter.submitData(this@MainActivity.lifecycle, value) },
            { error -> Log.i("TAG", "onCreate: ${error.localizedMessage}") }
        )

        Observable.create<String> {
            binding.autoCompleteTextView.doOnTextChanged { text, _, _, _ ->
                it.onNext(text.toString())
            }
        }
            .map { it.toLowerCase(Locale.ROOT).trim() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.updateAutoComplete(it) }

        viewModel.autoComplete.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
            binding.autoCompleteTextView.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        })

        adapter.addLoadStateListener { state ->
            val isUpdating = state.refresh == LoadState.Loading
            binding.loading.isVisible = isUpdating
            binding.searchRecyclerView.isVisible = !isUpdating
        }

        setContentView(binding.root)
    }
}