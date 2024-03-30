package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class DeleteRequest(context: Context) : ApiRequest(context){

    private fun deleteDataFromParameterAndId(urlParameter: String, id: String): MutableLiveData<String?> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urlForRequest = "$url$urlParameter/$id"
                val request = URL(urlForRequest)
                val conn = request.openConnection() as HttpURLConnection
                conn.requestMethod = "DELETE"
                conn.connect()

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    responseData.postValue("Deletion successful")
                } else {
                    responseData.postValue("Deletion failed: Response code $responseCode")
                }
            } catch (e: Exception) {
                Log.v("ApiRequest", e.toString())
                responseData.postValue("Error deleting data: ${e.message}")
            }
        }
        return responseData
    }

     fun deleteDiversById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/adherents", id)
    }

     fun deleteDivesById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/plongees", id)
    }

     fun deleteBoatById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/boats", id)
    }

     fun deleteLocationById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/lieux", id)
    }

     fun deleteMomentById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/moments", id)
    }

     fun deleteLevelById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/niveaux", id)
    }

     fun deleteDivesiteById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/lieux", id)
    }

     fun deleteParticipantById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/participants", id)
    }

     fun deletePersonById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/personnes", id)
    }

     fun deleteDiveById(id: String): MutableLiveData<String?> {
        return deleteDataFromParameterAndId("/plongees", id)
    }

}