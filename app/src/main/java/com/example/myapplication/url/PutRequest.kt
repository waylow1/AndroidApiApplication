package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PutRequest(context: Context) : ApiRequest(context) {

    fun putDataById(urlParameter: String, id: String, jsonData: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urlForRequest = "$url$urlParameter/$id"
                val request = URL(urlForRequest)
                val conn = request.openConnection() as HttpURLConnection
                conn.requestMethod = "PUT"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                val outputStream = conn.outputStream
                outputStream.write(jsonData.toString().toByteArray())
                outputStream.flush()
                outputStream.close()
                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    responseData.postValue("Modification successful")
                } else {
                    responseData.postValue("Modification failed: Response code $responseCode")
                }
            } catch (e: Exception) {
                Log.v("ApiRequest", e.toString())
                responseData.postValue("Error modifying data: ${e.message}")
            }
        }
    }

    fun editDiverById(id : String,jsonObject: JSONObject){
        putDataById("/adherents",id,jsonObject)
    }

    fun editBoatById(id : String,jsonObject: JSONObject){
        putDataById("/bateaux",id,jsonObject)
    }

    fun editLocationById(id : String,jsonObject: JSONObject){
        putDataById("/lieux",id,jsonObject)
    }

    fun editDiveById(id : String,jsonObject: JSONObject){
        putDataById("/plongees",id,jsonObject)
    }

}