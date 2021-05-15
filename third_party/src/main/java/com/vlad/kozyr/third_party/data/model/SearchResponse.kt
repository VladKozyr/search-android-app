package com.vlad.kozyr.third_party.data.model

data class SearchResponse(
    val _type: String,
    val didUMean: String,
    val relatedSearch: List<String>,
    val totalCount: Int,
    val value: List<SearchResult>
)