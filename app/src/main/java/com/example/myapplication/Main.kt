package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.url.ApiRequest
import com.example.myapplication.url.GetRequest
import org.json.JSONArray
import org.json.JSONObject

class Main : ComponentActivity() {

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_RUN_KEY = "isFirstRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true)

        if(isFirstRun){
            prefs.edit().putBoolean("isFirstRun",false).commit()
        }

        setContent { MyApplicationTheme {

                val apiResult = GetRequest(this);

                val login = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val data = apiResult.getAllDivers().observeAsState()
                    if(data.value!=null){
                        DataDisplay(JSONArray(data.value!!))
                    }

                }
            }
        }
    }
}


@Composable
fun DataDisplay(data: JSONArray){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        for( i in 0 until data.length()){
            DiverCard(data = data.getJSONObject(i))
        }
    }
}

@Composable
fun DiverCard(data: JSONObject) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.padding(20.dp).fillMaxWidth(0.93f)
    ) {
        Text(
            text = data.getString("prenom") + " " + data.getString("nom").uppercase(),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Left
        )
    }
}
