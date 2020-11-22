package com.andres.maca.newslist.datalayer

import androidx.lifecycle.LiveData
import com.andres.maca.newslist.datalayer.model.NewsItem

interface AppRepository {
    fun deleteNew(storyID: Int)
    fun getAllNews(): LiveData<List<NewsItem>>
    fun clean()
}