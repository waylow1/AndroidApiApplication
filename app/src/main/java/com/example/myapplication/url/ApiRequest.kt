package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    init {

        val resources = context.resources
        url = resources.getString(R.string.url)
    }

    private fun getData(urlParameter : String):String{
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
            }
            catch(e : Exception){
                Log.v("ApiRequest",e.toString())
            }
        }
        return response.toString()
    }

    public fun getAllDivers():String{
        return getData("/adherents")
    }

    public fun getAllDives():String{
        return getData("/plongees")
    }


}
