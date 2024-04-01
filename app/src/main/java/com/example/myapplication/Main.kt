package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.Functions.UsefulTools
import com.example.myapplication.bdd.MyAppDatabase
import com.example.myapplication.components.Navigation
import com.example.myapplication.pages.AddDiveButton
import com.example.myapplication.pages.DiveCreation
import com.example.myapplication.pages.DiveList
import com.example.myapplication.pages.DiveModification
import com.example.myapplication.pages.DiverCreation
import com.example.myapplication.pages.DiverList
import com.example.myapplication.pages.DiverModification
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.url.GetRequest
import kotlinx.coroutines.launch
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.json.JSONArray




class Main : ComponentActivity() {

    companion object{
        private val PREFS_NAME = "MyPrefsFile"
        private val FIRST_RUN_KEY = "isFirstRun"
    }

    private val page = mutableStateOf(Pages.DiverList)
    private val diverID = mutableIntStateOf(0)
    private val diveID = mutableIntStateOf(0)
    private lateinit var db: MyAppDatabase
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(page.value == Pages.DiverList) { //home page then quit
                finish()
            } else {
                if(page.value == Pages.DiveList || page.value == Pages.Stats) {
                    page.value = Pages.DiverList
                }

                else {
                    if(page.value == Pages.DiverCreation || page.value == Pages.DiverModification) {
                        page.value = Pages.DiverList
                    }
                    else {
                        page.value = Pages.DiveList
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

        db = MyAppDatabase.getDatabase(this);

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true)
        if(isFirstRun){
            val apiRequestAllLevels = GetRequest(this)
            val levelDao = db.levelDao()
            apiRequestAllLevels.getLiveData().observe(this) { levels ->
                if(levels!=null){
                    lifecycleScope.launch {
                        val levelsList = UsefulTools.parseJsonToLevels(levels)
                        for (level in levelsList) {
                            levelDao.insert(level)
                        }
                    }
                }
            }
            apiRequestAllLevels.getAllLevels()
            prefs.edit().putBoolean(FIRST_RUN_KEY, false).apply()
        }

        setContent { MyApplicationTheme {
            val apiResultDivers = GetRequest(this);
            val apiResultDiversId = GetRequest(this);
            val apiResultDetails = GetRequest(this);
            val apiResultDives = GetRequest(this);
            val apiResultLocation = GetRequest(this);
            val apiResultForADive = GetRequest(this);

            val divers = apiResultDivers.getLiveData().observeAsState();
            val details = apiResultDetails.getLiveData().observeAsState();
            val diver = apiResultDiversId.getLiveData().observeAsState();
            val dives = apiResultDives.getLiveData().observeAsState()
            val sites = apiResultLocation.getLiveData().observeAsState()
            val dive = apiResultForADive.getLiveData().observeAsState()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    bottomBar = {
                        Navigation(
                            page.value,
                            updatePage = {
                                newPage: Pages -> page.value = newPage
                            }
                        )
                    },
                    content = { padding ->
                        Box(
                            modifier = Modifier
                                .padding(padding)
                                .padding(top = 31.dp)
                        ) {
                            when(page.value) {
                                Pages.DiverList -> {
                                    apiResultDivers.getAllDivers()
                                    apiResultDetails.getDiverDetails()
                                    if(divers.value != null && details.value != null){
                                        DiverList(
                                            updatePage = {
                                                    newPage: Pages -> page.value = newPage
                                                apiResultDiversId.getDiverById(diverID.intValue.toString())
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
                                    if(diver.value != null) {
                                        DiverModification(
                                            diver.value!!,
                                            updatePage = {
                                                newPage: Pages -> page.value = newPage
                                            apiResultForADive.getDiveById(diveID.intValue.toString())
                                        }
                                    )
                                    } else {
                                        CircularProgressIndicator()
                                    }
                                }
                                Pages.DiverCreation -> DiverCreation(
                                    updatePage = {
                                            newPage: Pages -> page.value = newPage
                                        apiResultForADive.getDiveById(diveID.intValue.toString())
                                    }
                                )
                                Pages.DiveList -> {
                                    apiResultDives.getAllDives()
                                    apiResultLocation.getAllLocations()
                                    if(dives.value != null && sites.value != null) {
                                        DiveList(
                                            updatePage = {
                                                    newPage: Pages -> page.value = newPage
                                                apiResultForADive.getDiveById(diveID.intValue.toString())
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
                                    if(dive.value != null){
                                        DiveModification(
                                            dive.value!!,
                                            updatePage = {
                                                    newPage: Pages -> page.value = newPage
                                                apiResultForADive.getDiveById(diveID.intValue.toString())
                                            }
                                        )
                                    } else {
                                        CircularProgressIndicator()
                                    }
                                }
                                Pages.DiveCreation -> DiveCreation(
                                    updatePage = {
                                            newPage: Pages -> page.value = newPage
                                        apiResultForADive.getDiveById(diveID.intValue.toString())
                                    }
                                )
                                Pages.Stats -> {

                                    val diversJson = JSONArray(divers.value!!)
                                    val statData = ArrayList<BarEntry>()

                                    for( i in 0 until 14){
                                        var count = 0

                                        for( j in 0 until diversJson.length()){
                                            val currentDiver = diversJson.getJSONObject(j)
                                            if(currentDiver.getString("niveau").toInt() == i) {
                                                count++
                                            }
                                        }
                                        val entry = BarEntry(i.toFloat(), count.toFloat())
                                        statData.add(entry)
                                    }

                                    val dataSet = BarDataSet(statData, "Nombre de plongeur par niveau")
                                    val data = BarData(dataSet)

                                    AndroidView(
                                        factory = {
                                                context ->
                                            val view = LayoutInflater.from(context).inflate(R.layout.stats, null, true)
                                            val chart = view.findViewById<BarChart>(R.id.martin)
                                            chart.data = data
                                            chart.setMaxVisibleValueCount(14)
                                            chart.invalidate()
                                            view
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
                }
            }
        }
    }
}

