package com.andres.maca.newslist.views.NewsList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres.maca.newslist.R
import com.andres.maca.newslist.datalayer.model.NewsItem
import kotlinx.android.synthetic.main.news_item.view.*

class NewsListAdapter(private var newsList : ArrayList<NewsItem>): RecyclerView.Adapter<NewsListAdapter.NewListHolder>(){
    public class NewListHolder(newListView: View): RecyclerView.ViewHolder(newListView){
        var authorTextView: TextView = newListView.findViewById(R.id.author)
        var createdAtTextView: TextView = newListView.findViewById(R.id.created_at)
        var storyTitleTextView: TextView = newListView.findViewById(R.id.story_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewListHolder {
        val newsView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewListHolder(newsView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewListHolder, position: Int) {
        var currentNew = newsList.get(position)
        holder.authorTextView.text = currentNew.author
        holder.createdAtTextView.text = currentNew.createdAt.toString()
        holder.storyTitleTextView.text = currentNew.storyTitle
        holder.itemView.setOnClickListener { view->
            Log.d("Adapter", "url: "+currentNew.storyURL)
        }

    }

}