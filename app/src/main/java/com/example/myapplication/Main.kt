package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.NavigationDrawer
import com.example.myapplication.pages.DiveCreation
import com.example.myapplication.pages.DiveList
import com.example.myapplication.pages.DiveModification
import com.example.myapplication.pages.DiverCreation
import com.example.myapplication.pages.DiverList
import com.example.myapplication.pages.DiverModification
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.url.GetRequest
import org.json.JSONArray

class Main : ComponentActivity() {

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_RUN_KEY = "isFirstRun"
    private val page = mutableStateOf(Pages.DiverList)
    private val diverID = mutableIntStateOf(0)
    private val diveID = mutableIntStateOf(0)
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(page.value == Pages.DiverList) { //home page then quit
                finish()
            } else {
                page.value = Pages.DiverList
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true)

        if(isFirstRun){
            prefs.edit().putBoolean("isFirstRun",false).apply()
        }

        setContent { MyApplicationTheme {
                val apiResultDivers = GetRequest(this);
                val apiResultDiversId = GetRequest(this);
                val apiResultDetails = GetRequest(this);
                val apiResultDives = GetRequest(this);
                val apiResultLocation = GetRequest(this);
                val apiResultForADive = GetRequest(this);
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 31.dp)
                    ) {
                        when(page.value) {
                            Pages.DiverList -> {
                                val divers = apiResultDivers.getAllDivers().observeAsState()
                                val details = apiResultDetails.getDiverDetails().observeAsState()
                                if(divers.value != null && details.value != null){
                                    DiverList(
                                        updatePage = {
                                                newPage: Pages -> page.value = newPage
                                        },
                                        updateId = {
                                                newId: Int -> diverID.intValue = newId
                                        },
                                        divers = JSONArray(divers.value!!),
                                        details = JSONArray(details.value!!)
                                    )
                                }
                            }
                            Pages.DiverModification -> {
                                val diver = apiResultDiversId.getDiverById(diverID.intValue.toString()).observeAsState()
                                if(diver.value != null) {
                                    DiverModification(diver.value!!)
                                }
                            }
                            Pages.DiverCreation -> DiverCreation()
                            Pages.DiveList -> {
                                val dives = apiResultDives.getAllDives().observeAsState()
                                val sites = apiResultLocation.getAllLocations().observeAsState()
                                if(dives.value != null && sites.value != null) {
                                    DiveList(
                                        updatePage = {
                                                newPage: Pages -> page.value = newPage
                                        },
                                        updateId = {
                                                newId: Int -> diveID.intValue = newId
                                        },
                                        dives = JSONArray(dives.value!!),
                                        sites = JSONArray(sites.value!!)
                                    )
                                }
                            }
                            Pages.DiveModification -> {
                                val dive = apiResultForADive.getDiveById(diveID.intValue.toString()).observeAsState()
                                Log.v("test",dive.value.toString())
                                if(dive.value!=null){
                                    DiveModification(dive.value!!)
                                }
                            }
                            Pages.DiveCreation -> DiveCreation()
                        }
                    }

                    NavigationDrawer(
                        updatePage = {
                                newPage: Pages -> page.value = newPage
                        }
                    )
                }
            }
        }
    }
}