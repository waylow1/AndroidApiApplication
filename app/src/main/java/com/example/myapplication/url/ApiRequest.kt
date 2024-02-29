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

class ApiRequest(context: Context) : ViewModel() {

    private val url: String
    private val responseData =  MutableLiveData<String?>(null)


    init {

        val resources = context.resources
        url = BuildConfig.url
    }

    private fun getData(urlParameter : String):MutableLiveData<String?>{
        val response = StringBuilder()
        viewModelScope.launch(Dispatchers.IO){
            try{
                val urlForRequest = url+urlParameter
                val request = URL(urlForRequest)
                val conn = request.openConnection() as HttpURLConnection
                conn.requestMethod="GET"
                conn.connect()
                val inputStream = conn.inputStream
                val buffer = BufferedReader(InputStreamReader(inputStream))
                var line : String?
                while (buffer.readLine().also { line = it } != null) {
                    response.append(line).append("\n")
                }
                inputStream.close()
                responseData.postValue(response.toString())
            }
            catch(e : Exception){
                Log.v("ApiRequest",e.toString())
            }
        }
        return responseData
    }

    public fun getAllDivers():MutableLiveData<String?>{
        return getData("/adherents")
    }

    public fun getAllDives():MutableLiveData<String?>{
        return getData("/plongees")
    }


}
