package com.andres.maca.newslist.datalayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "deleted_hacker_news_table")
data class DeletedNews (@PrimaryKey(autoGenerate = false) @Json(name = "story_id") var storyId: Int)
