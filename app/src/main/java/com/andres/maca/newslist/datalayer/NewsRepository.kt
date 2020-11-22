package com.andres.maca.newslist.datalayer

import androidx.lifecycle.LiveData
import com.andres.maca.newslist.datalayer.model.DeletedNews
import com.andres.maca.newslist.datalayer.model.NewsDatabaseDao
import com.andres.maca.newslist.datalayer.model.NewsItem
import com.andres.maca.newslist.datalayer.network.ApiCloud
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(val apiCloud: ApiCloud, val database: NewsDatabaseDao): AppRepository{
    private val newsItems: LiveData<List<NewsItem>> by lazy{ database.getAllNews()}
    private val deletedNews: LiveData<List<DeletedNews>> by lazy{ database.getAllDeletedNews()}
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val newNewsListener = ArrayList<OnNewNews>()
    private var localStoryIDs: HashSet<Int> = HashSet()
    private var deletedStoriesIDs: HashSet<Int> = HashSet()
    init{
        newsItems
        deletedNews
        //updateNews()

    }

    override fun deleteNew(storyID: Int) {
        scope.launch {
            withContext(Dispatchers.IO){
                database.deleteNews(storyID)
                database.insert(DeletedNews(storyID))
                localStoryIDs.remove(storyID)
                deletedStoriesIDs.add(storyID)
                notifyNewsListener(true, "Deleted")

            }
        }
    }



    fun updateNews(){
        val localNews = newsItems.value
        val deletedNews = deletedNews.value
        if (deletedNews != null) {
            for (item: DeletedNews in deletedNews){
                deletedStoriesIDs.add(item.storyId)
            }
        }

        if (localNews != null) {
            for (item: NewsItem in localNews){
                localStoryIDs.add(item.storyId)
            }
        }
        scope.launch {
            withContext(Dispatchers.IO){ //Ok this is a O(n) algo to check if a item exists locally or not.
                //this it's faster to make a Database Replace All approach.
                try {
                    val onlineNews = apiCloud.getNews()
                    for (onlineNew: NewsItem in onlineNews.newNews){
                        if(!localStoryIDs.contains(onlineNew.storyId) && !deletedStoriesIDs.contains(onlineNew.storyId)){
                            database.insert(onlineNew)
                        }
                    }
                    notifyNewsListener(true, "News Updated")
                }catch (e:Exception){
                    notifyNewsListener(false, "Network Troubles ⛈😧")
                    //Not network Exception
                }
            }
        }
    }

    fun addNewsListener(newNews: OnNewNews){
        newNewsListener.add(newNews)
    }

    private fun notifyNewsListener(sucess: Boolean, message: String) {
        newNewsListener.forEach{listener ->
            listener.fetchComplete(sucess, message)
        }
    }

    override fun getAllNews(): LiveData<List<NewsItem>> {
        return newsItems
    }

    override fun clean() {
        newNewsListener.clear()
        scope.cancel()
    }

}