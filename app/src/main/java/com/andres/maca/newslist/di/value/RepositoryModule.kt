package com.andres.maca.newslist.di.value

import android.content.Context
import com.andres.maca.newslist.datalayer.model.NewsDatabase
import com.andres.maca.newslist.datalayer.model.NewsDatabaseDao
import com.andres.maca.newslist.datalayer.network.ApiCloud
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object RepositoryModule {
    const val BASE_URL = "https://hn.algolia.com/api/v1/"

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideHackerNewsAPI(retrofit: Retrofit): ApiCloud{
        return retrofit.create(ApiCloud::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()))
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            response

        }.build())
        .build()

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesDatabase(appContext: Context) = NewsDatabase.getInstance(context = appContext)

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesDatabaseDao(database: NewsDatabase) = database.newsDatabaseDao
}