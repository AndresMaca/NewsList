package com.andres.maca.newslist.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andres.maca.newslist.R
import com.andres.maca.newslist.views.NewsList.NewsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment = NewsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, currentFragment!!).commit()

        //TODO: The api is returning several stories with the same title + nothing to do here..
        //TODO: Empirically the created_by should be the primary key because the API behavior is so
        //strange...
    }
}