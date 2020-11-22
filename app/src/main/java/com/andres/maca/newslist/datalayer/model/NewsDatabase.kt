package com.andres.maca.newslist.datalayer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [NewsItem::class, DeletedNews::class], version = 1, exportSchema = false)
abstract class NewsDatabase: RoomDatabase(){
    abstract val newsDatabaseDao: NewsDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: NewsDatabase? = null
        fun getInstance(context: Context): NewsDatabase{
            synchronized(this){
                var instance = INSTANCE;
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "hacker_news_database").build()
                }
                return instance;
            }
        }
    }
}