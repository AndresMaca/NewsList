package com.andres.maca.newslist.datalayer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "hacker_news_table")
data class NewsItem (@PrimaryKey(autoGenerate = false) @Json(name = "story_id") var storyId: Int,
                     @ColumnInfo(name = "story_title") @Json(name = "story_title") var storyTitle: String,
                     @ColumnInfo(name = "author") @Json(name="author") var author: String,
                     @ColumnInfo(name = "story_url") @Json(name = "story_url") var storyURL: String,
                     @ColumnInfo(name = "created_at") @Json(name = "created_at") var createdAt: Date
){
    override fun equals(other: Any?): Boolean {
        return if (other is NewsItem){
            storyId == other.storyId
        }else{
            false
        }
    }


}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}



//StoryTitle/Title

//Author/CreatedAt.

//Story_URL

//Deleted_URL.