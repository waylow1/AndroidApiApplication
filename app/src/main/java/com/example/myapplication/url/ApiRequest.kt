package com.example.myapplication.url

import android.content.Context
import android.util.Log
import com.example.myapplication.R

class ApiRequest(context: Context){

    private val url: String

    init {

        val resources = context.resources
        url = resources.getString(R.string.url)
    }

    public fun connectionTest():String{
        return url
    }

}
