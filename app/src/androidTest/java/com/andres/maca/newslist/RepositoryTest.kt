package com.andres.maca.newslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.andres.maca.newslist.datalayer.NewsRepository
import com.andres.maca.newslist.datalayer.OnNewNews
import com.andres.maca.newslist.datalayer.model.DeletedNews
import com.andres.maca.newslist.datalayer.model.NewsDatabase
import com.andres.maca.newslist.datalayer.model.NewsItem
import com.andres.maca.newslist.datalayer.network.ApiCloud
import com.andres.maca.newslist.di.value.RepositoryModule
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class RepositoryTest{
    private lateinit var newsRepository: NewsRepository
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createRepo(){
        val hackerNewsServer = object : ApiCloud {
            override suspend fun getNews(): List<NewsItem>  =
                withContext(Dispatchers.IO) {
                    var news = ArrayList<NewsItem>()
                    for (i in 1..10) {
                        news.add(
                            NewsItem(123 + i, "title" + i, "author" + i, "url" + i,
                            Date()
                        )
                        )
                    }
                    news
                }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val newsDatabase = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).allowMainThreadQueries().build()
        val newsDatabaseDao = newsDatabase.newsDatabaseDao
        newsRepository = NewsRepository(hackerNewsServer, newsDatabaseDao)

    }


    @Test
    fun updateListenerTest()= runBlocking{
        val latch: CountDownLatch = CountDownLatch(1)

        val news = newsRepository.getAllNews()
        val listener = object: OnNewNews{
            override fun fetchComplete(success: Boolean, message: String) {
                news.observeForever {  }
                assertTrue(news.value!!.isNotEmpty())
                latch.countDown() // notify the count down latch
            }
        }

        newsRepository.addNewsListener(listener)

        newsRepository.updateNews()
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun deleteNewsAndUpdateTest()= runBlocking{
        var latch: CountDownLatch = CountDownLatch(1)
        val listener = object: OnNewNews{
            override fun fetchComplete(success: Boolean, message: String) {
                latch.countDown() // notify the count down latch
            }
        }
        newsRepository.addNewsListener(listener)
        newsRepository.updateNews()
        val news = newsRepository.getAllNews()

        try {
        latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        latch = CountDownLatch(1)

        news.observeForever {  }

        val deleteNew = DeletedNews(news.value!![0].storyId)
        newsRepository.deleteNew(deleteNew.storyId)
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        newsRepository.updateNews()


        latch = CountDownLatch(1)

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val localStoryIDs = HashSet<Int>()

        for (news: NewsItem in news.value!!){
            localStoryIDs.add(news.storyId)
        }

        if(localStoryIDs.contains(deleteNew.storyId)){
            fail()
        }

    }

    @Test
    fun networkTest() = runBlocking{
        var server = RepositoryModule.provideHackerNewsAPI(RepositoryModule.provideRetrofitInterface())
        var news = server.getNews()
        assertTrue(news.isNotEmpty())

    }


}