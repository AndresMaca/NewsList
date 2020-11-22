package com.andres.maca.newslist.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andres.maca.newslist.di.component.DaggerViewModelInjector
import com.andres.maca.newslist.di.component.ViewModelInjector
import com.andres.maca.newslist.di.value.RepositoryModule
import com.andres.maca.newslist.views.NewsList.NewsListViewModel


abstract class BaseViewModel(application: Application) : AndroidViewModel(application){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .context(application)
        .repositoryModule(RepositoryModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is NewsListViewModel -> injector.inject(this)
        }
    }
}