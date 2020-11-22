package com.andres.maca.newslist.datalayer

interface OnNewNews {
    fun fetchComplete(success:Boolean, message: String)
}
