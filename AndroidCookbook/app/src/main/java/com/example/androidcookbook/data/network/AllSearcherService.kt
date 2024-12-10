package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.search.SearchAll
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AllSearcherService {
    @GET("search/all/{query}")
    suspend fun searchAll(@Path("query") query: String): ApiResponse<SearchAll>


}