package com.andres.maca.newslist.di.value

import android.content.Context
import com.andres.maca.newslist.datalayer.model.NewsDatabase
import com.andres.maca.newslist.datalayer.model.NewsDatabaseDao
import com.andres.maca.newslist.datalayer.model.NewsItem
import com.andres.maca.newslist.datalayer.network.ApiCloud
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

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
                    .add(NewsItemsAdapter())
                    .add(KotlinJsonAdapterFactory())
                    .build()))
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            response

        }.build())
        .build()

    data class NewsItemsJson(@Json(name = "story_id") var storyId: String?,
                             @Json(name = "object_id") var objectId: String?,
                             @Json(name = "objectID") var objectID: String?,
                             @Json(name = "story_title") var storyTitle: String?,
                             @Json(name = "title") var title: String?,
                             @Json(name="author") var author: String,
                             @Json(name = "story_url") val storyUrl: String?,
                             @Json(name = "url") val url: String?,
                             @Json(name = "created_at") val createdAt: String) //To avoid boilerplate code.
    class NewsItemsAdapter{
        @FromJson fun fromJson(data: NewsItemsJson): NewsItem{//Internally it gives a hand to the autogen
            //code to get the correct data, because the API is a mess with keys ie. story_id and objectID and object_id
            //the id can come in any one of these...
            //Anyways I test it with around 10000 register and the cases are covered by sunday 22 /11/2020
            var storyId: Int? =null
            when {
                data.storyId != null -> {
                    storyId = data.storyId!!.toInt()
                }
                data.objectId != null -> {
                    storyId = data.objectId!!.toInt()
                }
                data.objectID != null -> {
                    storyId = data.objectID!!.toInt()
                }
            }
            var storyUrl = ""
            when{
                data.storyUrl != null->{
                    storyUrl = data.storyUrl!!
                }
                data.url != null ->{
                    storyUrl = data.url!!
                }
            }
            var author = data.author
            var storyTitle = "";
            when{
                data.storyTitle != null ->{
                    storyTitle = data.storyTitle!!
                }data.title != null ->{
                    storyTitle = data.title!!
                }
            }
            var createdAt = if( data.createdAt.length>20) 1.toLong() else 2.toLong()
            return NewsItem(storyId!!,storyTitle,author,storyUrl,createdAt)
        }


    }
    @Provides
    @Reusable
    @JvmStatic
    internal fun providesDatabase(appContext: Context) = NewsDatabase.getInstance(context = appContext)

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesDatabaseDao(database: NewsDatabase) = database.newsDatabaseDao
}