package com.andres.maca.newslist.datalayer.network

import com.andres.maca.newslist.datalayer.model.NewsItemList
import retrofit2.http.GET

interface ApiCloud{

    @GET("search_by_date?query=android") //
       //@GET("search_by_date?query=android&hitsPerPage=10000")
    suspend fun getNews(): NewsItemList


}