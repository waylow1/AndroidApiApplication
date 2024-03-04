package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val data = apiResult.getAllDives().observeAsState()
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
    Column {
        Text(text = "1", color = Color.Blue)
        for( i in 0 until data.length()){
            Text(text = data.getJSONObject(i).getInt("id").toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val context = LocalContext.current;
        val apiResult = GetRequest(context);

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val data = apiResult.getAllDives().observeAsState()
            if(data.value!=null){
                DataDisplay(JSONArray(data.value!!))
            }
        }
    }
}