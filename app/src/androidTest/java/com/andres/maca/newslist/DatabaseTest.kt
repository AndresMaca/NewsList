package com.andres.maca.newslist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.andres.maca.newslist.datalayer.model.*
import com.andres.maca.newslist.datalayer.network.ApiCloud
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@RunWith(AndroidJUnit4::class)
class DatabaseTest{
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsDatabaseDao: NewsDatabaseDao
    private lateinit var hackerNewsServer: ApiCloud
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun createDatabase(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        newsDatabase = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).allowMainThreadQueries().build()
        newsDatabaseDao = newsDatabase.newsDatabaseDao
        hackerNewsServer = object : ApiCloud {
            override suspend fun getNews(): NewsItemList =
                withContext(Dispatchers.IO) {
                    var news = ArrayList<NewsItem>()
                    for (i in 1..10) {
                        news.add(
                            NewsItem(123 + i, "title" + i, "author" + i, "url" + i,
                                i.toLong()
                            )
                        )
                    }
                    NewsItemList(news)
                }
        }

    }
    @Test
    fun compareDifferentNews(){
        assertFalse(NewsItem(1,"asd","as","321", 1.toLong())==NewsItem(2,"asd","as","321", 1.toLong()))
    }
    @Test
    fun saveValuesCorrectlyTest() = runBlocking {
        val hackers = hackerNewsServer.getNews().newNews
        hackers.forEach{ hackerNew -> newsDatabaseDao.insert(hackerNew)}

        val localHackers = newsDatabaseDao.getAllNews()
        localHackers.observeForever {  }
        assertTrue(localHackers.value == hackers)
    }
    @Test
    fun deleteANew() = runBlocking {
        val hackersNews = hackerNewsServer.getNews().newNews
        hackersNews.forEach{ hackerNew -> newsDatabaseDao.insert(hackerNew)}

        val localHackersNews = newsDatabaseDao.getAllNews()
        localHackersNews.observeForever {  }
        val deletedNew = localHackersNews.value!!.get(0)


        newsDatabaseDao.deleteNews(deletedNew.storyId)

        val localStoryIDs = HashSet<Int>()


        for (localNew: NewsItem in localHackersNews.value!!){
           localStoryIDs.add(localNew.storyId)
        }
        if(localStoryIDs.contains(deletedNew.storyId)){
            fail()
        }

    }

    @Test
    fun addDeleteNews(){
        val storyID = 123
        newsDatabaseDao.insert(DeletedNews(storyID))
        val deletedNews = newsDatabaseDao.getAllDeletedNews()
        deletedNews.observeForever {  }

        assertTrue(deletedNews.value!!.get(0).storyId == storyID)
    }

}