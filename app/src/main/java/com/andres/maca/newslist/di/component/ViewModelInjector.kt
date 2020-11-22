package com.andres.maca.newslist.di.component

import android.content.Context
import com.andres.maca.newslist.di.value.RepositoryModule
import com.andres.maca.newslist.views.NewsList.NewsListViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(RepositoryModule::class)])
interface ViewModelInjector {

    /*
    This dagger-lifecycle is based on viewmodel-lifecycle. This is a Good One in order to code small apps.
    It's perfectly possible attach the viewmodel to the Android APP lifecycle and it persists to activity + fragments
    changes.
     */

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun context(@BindsInstance context: Context): Builder

        fun repositoryModule(repositoryModule: RepositoryModule): Builder

    }
    fun inject(newsListViewModel: NewsListViewModel)

}