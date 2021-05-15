package com.vlad.kozyr.third_party.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vlad.kozyr.third_party.databinding.ErrorItemBinding
import com.vlad.kozyr.third_party.databinding.LoadingItemBinding

class SearchLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<SearchLoadStateAdapter.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState): Int {
        return when (loadState) {
            is LoadState.Error -> 1
            is LoadState.Loading -> 0
            else -> error("Not supporting...")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return when (loadState) {
            is LoadState.NotLoading -> error("Not supporting...")
            is LoadState.Loading -> LoadingViewHolder(
                LoadingItemBinding.inflate(layoutInflater, parent, false)
            )
            is LoadState.Error -> ErrorViewHolder(
                ErrorItemBinding.inflate(layoutInflater, parent, false),
                retry
            )
        }
    }

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(state: LoadState)
    }

    class LoadingViewHolder(binding: LoadingItemBinding) : ItemViewHolder(binding.root) {
        override fun bind(state: LoadState) {
            // TODO("Not yet implemented")
        }
    }

    class ErrorViewHolder(private val binding: ErrorItemBinding, private val retry: () -> Unit) :
        ItemViewHolder(binding.root) {
        override fun bind(state: LoadState) {
            binding.button.setOnClickListener { retry.invoke() }
        }
    }
}