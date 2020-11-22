package com.andres.maca.newslist.views.NewsList

import android.app.Application
import com.andres.maca.newslist.datalayer.NewsRepository
import com.andres.maca.newslist.views.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class NewsListViewModel @Inject constructor(application: Application) : BaseViewModel(application){
    @Inject
    lateinit var appRepository: NewsRepository

    private val viewModelJob = Job()

}