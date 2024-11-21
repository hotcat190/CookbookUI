package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.SearchService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchService: SearchService
) {
    suspend fun search(searchQuery: String) = searchService.search(searchQuery)
}
