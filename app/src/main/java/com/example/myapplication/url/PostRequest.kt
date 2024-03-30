package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PostRequest(context: Context) : ApiRequest(context) {

    fun postAnyData(urlParameter: String, jsonData: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urlForRequest = "$url$urlParameter"
                val request = URL(urlForRequest)
                val conn = request.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                conn.outputStream.use { outputStream ->
                    val writer = OutputStreamWriter(outputStream)
                    writer.write(jsonData.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    responseData.postValue("Insertion successful")
                } else {
                    val errorMessage = conn.errorStream?.bufferedReader().use { it?.readText() }
                    responseData.postValue("Insertion failed: Response code $responseCode, Error: $errorMessage")
                }
            } catch (e: Exception) {
                Log.v("ApiRequest", e.toString())
                responseData.postValue("Error inserting data: ${e.message}")
            }
        }
    }

    fun insertDiver(jsonData: JSONObject){
        postAnyData("/adherents",jsonData);
    }
    fun insertDive(jsonData: JSONObject){
        postAnyData("/plongees",jsonData);
    }
    fun insertBoat(jsonData: JSONObject){
        postAnyData("/bateaux",jsonData);
    }
    fun insertLocation(jsonData: JSONObject){
        postAnyData("/lieux",jsonData);
    }
}



