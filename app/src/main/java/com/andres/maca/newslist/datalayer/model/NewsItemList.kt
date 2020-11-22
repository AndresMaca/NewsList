package com.andres.maca.newslist.datalayer.model

import com.andres.maca.newslist.datalayer.OnNewNews
import com.squareup.moshi.Json

data class NewsItemList (@Json(name = "hits") val newNews: List<NewsItem>)