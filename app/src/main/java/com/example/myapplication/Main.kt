package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
            prefs.edit().putBoolean("isFirstRun",false).apply()
        }

        setContent { MyApplicationTheme {
                val apiResult = GetRequest(this);
                val login = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val divers = apiResult.getAllDivers().observeAsState()
                    val details = apiResult.getDiverDetails().observeAsState()
                    if(divers.value != null && details.value != null){
                        DataDisplay(JSONArray(divers.value!!), JSONArray(details.value))
                    }
                }
            }
        }
    }
}


@Composable
fun DataDisplay(divers: JSONArray, details: JSONArray){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        for( i in 0 until divers.length()){
            var diverDetailIdx = 0
            val diver = divers.getJSONObject(i)
            for( j in 0 until details.length()){
               if(divers.getJSONObject(j).getString("id") == diver.getString("id")) {
                    diverDetailIdx = j
               }
            }
            DiverCard(diver = divers.getJSONObject(i), details.getJSONObject(diverDetailIdx))
        }
    }
}

@Composable
fun DiverCard(diver: JSONObject, details: JSONObject) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = diver.getString("prenom") + " " + diver.getString("nom").uppercase(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "  • ",
                    fontWeight = FontWeight.Bold,
                    color = (
                            if(details.getString("actif") == "true") {
                                Color.Green
                            } else {
                                Color.LightGray
                            }
                            ),
                    textAlign = TextAlign.End
                )
                Text(
                    text = (
                            if(details.getString("actif") == "true") {
                                "actif"
                            } else {
                                "inactif"
                            }
                            ),
                    textAlign = TextAlign.End
                )
            }
            Text(
                text = details.getString("email"),
                modifier = Modifier.padding(start = 16.dp),
                color = Color.Gray,
                textAlign = TextAlign.Left
            )
            Button(
                onClick = {
                    /*edit the diver*/
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "modifier"
                )
            }
        }
    }
}
