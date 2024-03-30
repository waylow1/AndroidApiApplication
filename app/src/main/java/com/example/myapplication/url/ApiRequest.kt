package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

open class ApiRequest(context: Context) : ViewModel() {

    protected val url: String
    protected val responseData =  MutableLiveData<String?>(null)

    init {

        val resources = context.resources
        url = BuildConfig.url
    }

    fun getLiveData() : MutableLiveData<String?>{
        return responseData;
    }

}
