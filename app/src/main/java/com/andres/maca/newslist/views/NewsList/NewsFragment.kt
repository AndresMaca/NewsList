package com.andres.maca.newslist.views.NewsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres.maca.newslist.R
import com.andres.maca.newslist.datalayer.model.NewsItem

class NewsFragment : Fragment(){
    private lateinit var newsListViewModel: NewsListViewModel
    private var newsItems = ArrayList<NewsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsListViewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)//
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val newsListView = inflater.inflate(R.layout.news_list_fragment, container, false)
        val newsListRecyclerView: RecyclerView = newsListView.findViewById(R.id.news_list_recycler_view)
        newsListRecyclerView.layoutManager = LinearLayoutManager(newsListRecyclerView.context)
        newsListRecyclerView.setHasFixedSize(true)
        val newsListAdapter = NewsListAdapter(newsItems)
        newsListRecyclerView.adapter = newsListAdapter
        newsListViewModel.allNews.observe(viewLifecycleOwner, Observer { news ->
            newsItems.clear()
            newsItems.addAll(news)
            newsListAdapter.notifyDataSetChanged()

        })

        return newsListView

    }

}