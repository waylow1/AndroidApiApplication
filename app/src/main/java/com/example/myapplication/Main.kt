package com.example.myapplication

import android.content.Context
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class Main : ComponentActivity() {

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_RUN_KEY = "isFirstRun"
    private val page = mutableStateOf(Pages.DiveList)
    private val id = mutableIntStateOf(0)
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
                val apiResultDetails = GetRequest(this);
                val apiResultDives = GetRequest(this);
                val apiResultLocation = GetRequest(this);
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
                                                newId: Int -> id.intValue = newId
                                        },
                                        divers = JSONArray(divers.value!!),
                                        details = JSONArray(details.value!!)
                                    )
                                }
                            }
                            Pages.DiverModification -> {
                                val diver = apiResultDivers.getDiverById(id.intValue.toString())
                                if(diver.value != null) {
                                    DiverModification(JSONArray(diver.value!!))
                                }
                            }
                            Pages.DiverCreation -> DiverCreation()
                            Pages.DiveList -> {
                                val dives = apiResultDives.getAllDives().observeAsState()
                                Thread.sleep(10000)
                                val sites = apiResultLocation.getAllLocations().observeAsState()
                                if(dives.value != null && sites.value != null) {
                                    DiveList(
                                        updatePage = {
                                                newPage: Pages -> page.value = newPage
                                        },
                                        updateId = {
                                                newId: Int -> id.intValue = newId
                                        },
                                        dives = JSONArray(dives.value!!),
                                        sites = JSONArray(sites.value!!)
                                    )
                                    Text(text = sites.value.toString())
                                }
                            }
                            Pages.DiveModification -> DiveModification()
                            Pages.DiveCreation -> DiveCreation()
                            Pages.SecuritySheet -> {}
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