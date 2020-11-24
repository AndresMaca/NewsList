package com.andres.maca.newslist.views.NewsList

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres.maca.newslist.R
import com.andres.maca.newslist.datalayer.model.NewsItem
import kotlinx.android.synthetic.main.news_list_fragment.*

class NewsFragment : Fragment(){
    private lateinit var newsListViewModel: NewsListViewModel
    private var newsItems = ArrayList<NewsItem>()
    private lateinit var deleteIcon: Drawable
    private lateinit var colorDrawableBackground: ColorDrawable

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
        newsListRecyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        colorDrawableBackground = ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)) //without theme
        deleteIcon = ContextCompat.getDrawable(activity!!.baseContext, R.drawable.ic_baseline_delete_forever_24)!!
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = newsListAdapter.removeItem(viewHolder.adapterPosition, viewHolder)
                newsListViewModel.deleteItem(id)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMarginVertical = (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    colorDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                        itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth, itemView.bottom - iconMarginVertical)
                } else {
                    colorDrawableBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth, itemView.top + iconMarginVertical,
                        itemView.right - iconMarginVertical, itemView.bottom - iconMarginVertical)
                    deleteIcon.level = 0
                }

                colorDrawableBackground.draw(c)

                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(newsListRecyclerView)
        return newsListView

    }

}