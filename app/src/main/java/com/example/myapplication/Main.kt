package com.example.myapplication

import android.content.Context
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.pages.DiveCreation
import com.example.myapplication.pages.DiveList
import com.example.myapplication.pages.DiveModification
import com.example.myapplication.pages.DiverCreation
import com.example.myapplication.pages.DiverList
import com.example.myapplication.pages.DiverModification
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.url.GetRequest
import org.json.JSONArray
import org.json.JSONObject

class Main : ComponentActivity() {

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_RUN_KEY = "isFirstRun"
    private val page = mutableStateOf(Pages.DiverList)
    private val id = mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true)

        if(isFirstRun){
            prefs.edit().putBoolean("isFirstRun",false).apply()
        }

        setContent { MyApplicationTheme {
                val apiResult = GetRequest(this);

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(page.value) {
                        Pages.DiverList -> {
                            val divers = apiResult.getAllDivers().observeAsState()
                            val details = apiResult.getDiverDetails().observeAsState()
                            if(divers.value != null && details.value != null){
                                DiverList(
                                    updatePage = {
                                                 newPage: Pages -> page.value = newPage
                                    },
                                    updateId = {
                                               newId: Int -> id.intValue = newId
                                    },
                                    divers = JSONArray(divers.value!!),
                                    details = JSONArray(details.value!!))
                            }
                        }
                        Pages.DiverModification -> {
                            val diver = apiResult.getDiverById(id.intValue.toString())
                            if(diver.value != null) {
                                DiverModification(id.intValue)
                            }
                        }
                        Pages.DiverCreation -> DiverCreation()
                        Pages.DiveList -> DiveList()
                        Pages.DiveModification -> DiveModification()
                        Pages.DiveCreation -> DiveCreation()
                        Pages.SecuritySheet -> {}
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(page.value == Pages.DiverList) {
            super.onBackPressed()
        } else {
            page.value = Pages.DiverList
        }
    }
}