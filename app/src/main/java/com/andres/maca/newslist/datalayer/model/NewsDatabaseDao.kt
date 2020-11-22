package com.andres.maca.newslist.datalayer.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDatabaseDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsItem: NewsItem)
    @Insert
    fun insert(deletedNews: DeletedNews)

    //It's possible to make a query who got all the news except the ones that are on DeletedNews
    // table in order to not  delete the new from hackerNewsTable and perform 2 queries but I think
    // it's better to keep the hacker_news_table clean without the deltedNews items.
    @Query("SELECT * FROM hacker_news_table ORDER BY created_at")
    fun getAllNews(): LiveData<List<NewsItem>>

    @Query("DELETE FROM hacker_news_table WHERE storyId = :storyId")
    fun deleteNews(storyId: Int)

    @Query("SELECT * FROM deleted_hacker_news_table")
    fun getAllDeletedNews(): LiveData<List<DeletedNews>>

}