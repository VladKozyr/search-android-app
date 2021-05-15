package com.vlad.kozyr.third_party.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vlad.kozyr.third_party.data.model.SearchResult
import com.vlad.kozyr.third_party.databinding.SearchResultItemBinding

class SearchResultAdapter :
    PagingDataAdapter<SearchResult, SearchResultAdapter.SearchResultViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            SearchResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class SearchResultViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SearchResult) {
            binding.result = result
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}