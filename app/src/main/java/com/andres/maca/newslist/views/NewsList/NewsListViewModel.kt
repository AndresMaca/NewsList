package com.andres.maca.newslist.views.NewsList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.andres.maca.newslist.datalayer.NewsRepository
import com.andres.maca.newslist.datalayer.OnNewNews
import com.andres.maca.newslist.views.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class NewsListViewModel @Inject constructor(application: Application) : BaseViewModel(application),
    OnNewNews {
    @Inject
    lateinit var appRepository: NewsRepository

    val allNews = appRepository.getAllNews()

    val updatedNewsNotifier = MutableLiveData<Boolean>()

    init {
        appRepository.updateNews()
    }

    fun updateData(){
        appRepository.updateNews()
    }
    override fun onCleared() {
        appRepository.clean()
        super.onCleared()
    }
    fun deleteItem(newsId : Int){
        appRepository.deleteNew(newsId)
    }

    override fun fetchComplete(success: Boolean, message: String) {
        updatedNewsNotifier.postValue(success)
    }

}