package com.andres.maca.newslist.datalayer.network

import com.andres.maca.newslist.datalayer.model.NewsItem
import retrofit2.http.GET

interface ApiCloud{

    @GET("/search_by_date?query=android")
    suspend fun getNews():List<NewsItem>


}