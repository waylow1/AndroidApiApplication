package com.example.myapplication.url

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class GetRequest(context: Context) : ApiRequest(context) {

    private fun getData(urlParameter : String): MutableLiveData<String?> {
        val response = StringBuilder()
        responseData.postValue(null)
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
            catch(_: Exception){

            }
        }
        return responseData
    }

     fun getAllDivers():MutableLiveData<String?>{
        return getData("/adherents")
    }

     fun getAllDives():MutableLiveData<String?>{
        return getData("/plongees")
    }

     fun getDiverDetails(): MutableLiveData<String?> {
        return getData("/adherents/details")
    }

     fun getInactiveDivers(): MutableLiveData<String?> {
        return getData("/adherents/inactifs")
    }

     fun getDiverById(id: String): MutableLiveData<String?> {
        return getData("/adherents/$id")
    }

     fun getDiverDetailsById(id: String): MutableLiveData<String?> {
        return getData("/adherents/$id/details")
    }

     fun getAllBoats(): MutableLiveData<String?> {
        return getData("/bateaux")
    }

     fun getBoatById(id: String): MutableLiveData<String?> {
        return getData("/bateaux/$id")
    }

     fun getAllLocations(): MutableLiveData<String?> {
        return getData("/lieux")
    }

     fun getLocationById(id: String): MutableLiveData<String?> {
        return getData("/lieux/$id")
    }

     fun getAllMoments(): MutableLiveData<String?> {
        return getData("/moments")
    }

     fun getMomentById(id: String): MutableLiveData<String?> {
        return getData("/moments/$id")
    }

     fun getAllLevels(): MutableLiveData<String?> {
        return getData("/niveaux")
    }

     fun getLevelById(id: String): MutableLiveData<String?> {
        return getData("/niveaux/$id")
    }

     fun getAllDiveSites(): MutableLiveData<String?> {
        return getData("/lieux")
    }

     fun getDiveSiteById(id: String): MutableLiveData<String?> {
        return getData("/lieux/$id")
    }

     fun getAllParticipants(): MutableLiveData<String?> {
        return getData("/participants")
    }

     fun getParticipantById(id: String): MutableLiveData<String?> {
        return getData("/participants/$id")
    }

     fun getAllPersons(): MutableLiveData<String?> {
        return getData("/personnes")
    }

     fun getPersonById(id: String): MutableLiveData<String?> {
        return getData("/personnes/$id")
    }

     fun getDiveById(id: String): MutableLiveData<String?> {
        return getData("/plongees/$id")
    }

}