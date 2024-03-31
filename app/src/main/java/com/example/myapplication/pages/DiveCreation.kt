package com.example.myapplication.pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.Functions.UsefulTools
import com.example.myapplication.bdd.Levels
import com.example.myapplication.bdd.MyAppDatabase
import com.example.myapplication.url.GetRequest
import com.example.myapplication.url.PostRequest
import com.example.myapplication.url.PutRequest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.typeOf

@Composable
fun DiveCreation() {

    val context = LocalContext.current

    val moments = listOf("Matin", "Après-midi", "Soir")

    val date = remember { mutableStateOf("") }
    val minPlongeurs = remember { mutableStateOf("") }
    val maxPlongeurs = remember { mutableStateOf("") }
    val pilote = remember { mutableStateOf("") }
    val securiteDeSurface = remember { mutableStateOf("") }
    val directeurDePlongee = remember { mutableStateOf("") }

    /* ApiRequest creations for  all Boats and all Locations */

    /* ApiRequest Object */
    val apiRequestAllBoats = GetRequest(context);
    val apiRequestLocations = GetRequest(context);
    val apiRequestDivers = GetRequest(context);

    /* LiveData observer */
    val boats = apiRequestAllBoats.getLiveData().observeAsState();
    val locations = apiRequestLocations.getLiveData().observeAsState();

    val allPersons = apiRequestDivers.getLiveData().observeAsState();

    /* List rembembers */
    val boatData = remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }
    val locationData = remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }


    /* Fetching Api */
    apiRequestAllBoats.getAllBoats();
    apiRequestLocations.getAllLocations();
    apiRequestDivers.getAllPersons();

    if(boats.value!=null){
        boatData.value = UsefulTools.parseBoatsJson(boats.value.toString())
    }

    if(locations.value!=null){
        locationData.value=UsefulTools.parseLocationsJson(locations.value.toString())
    }

    val pilotsMap = mutableMapOf<Int, String>()
    val securityMap = mutableMapOf<Int, String>()
    val directorsMap = mutableMapOf<Int, String>()

    if(allPersons.value!=null){
        val personsJsonString = allPersons.value.toString()
        val personsJsonArray = JSONArray(personsJsonString)
        for (i in 0 until personsJsonArray.length()) {
            val personJsonObject = personsJsonArray.getJSONObject(i)
            val id = personJsonObject.getInt("id")
            val nom = personJsonObject.getString("nom")

            if (personJsonObject.optBoolean("pilote", false)) {
                pilotsMap[id] = nom
            }
            if (personJsonObject.optBoolean("securite_de_surface", false)) {
                securityMap[id] = nom
            }
            if (personJsonObject.optBoolean("directeur_de_section", false)) {
                directorsMap[id] = nom
            }
        }
    }



    /* Database connection fetching all levels  */
    val db = MyAppDatabase.getDatabase(context)
    val levelDao = db.levelDao()
    val levels = remember { mutableStateOf<List<Levels>>(emptyList()) }
    LaunchedEffect(true) {
        levels.value = levelDao.getAllLevels()
    }

    /* Remembers for selected values */

    val selectedLevelIndex = remember {
        mutableIntStateOf(-1)
    }

    val selectedMoment = remember {
        mutableIntStateOf(-1);
    }

    val selectedLocationIndex = remember {
        mutableIntStateOf(-1)
    }

    val selectedBoatIndex = remember {
        mutableIntStateOf(-1)
    }

    /* Remembers for expanded value */

    val expandedBoat = remember {
        mutableStateOf(false)
    }

    val expandedLocation = remember {
        mutableStateOf(false)
    }

    val expandedLevel = remember {
        mutableStateOf(false)
    }

    val expandedMoment = remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Création d'une plongée",
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 31.dp, end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Date: ")
                TextField(
                    value = date.value,
                    onValueChange = {
                        date.value = it
                    }, placeholder = {Text(text = "Format: 2022-02-26")}
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Lieu: ")
                UsefulTools.DropdownMenuWithState(
                    items = locationData.value.map { it.second },
                    selectedValue = selectedLocationIndex,
                    expandedState = expandedLocation
                ) { index ->
                    Log.v("Selected Boat", locationData.value[index].toString())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Bateau: ")
                UsefulTools.DropdownMenuWithState(
                    items = boatData.value.map { it.second },
                    selectedValue = selectedBoatIndex,
                    expandedState = expandedBoat
                ) { index ->
                    // Logique à exécuter lorsque le bateau est sélectionné
                    Log.v("Selected Boat", boatData.value[index].toString())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Moment: ")
                UsefulTools.DropdownMenuWithState(
                    items = moments,
                    selectedValue = selectedMoment,
                    expandedState = expandedMoment
                ){
                    Log.v("Moment",selectedMoment.intValue.toString())
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nombre minimum de plongeurs: ")
                TextField(
                    value = minPlongeurs.value,
                    onValueChange = {
                        minPlongeurs.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nombre maximum de plongeurs: ")
                TextField(
                    value = maxPlongeurs.value,
                    onValueChange = {
                        maxPlongeurs.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Niveau: ")
                UsefulTools.DropdownMenuWithState(
                    items = levels.value.map { it.name },
                    selectedValue = selectedLevelIndex,
                    expandedState = expandedLevel
                ) { index ->
                    Log.v("Level",levels.value.get(selectedLevelIndex.intValue).name)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pilote: ")
                TextField(
                    value = pilote.value,
                    onValueChange = {
                        pilote.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sécurité de surface: ")
                TextField(
                    value = securiteDeSurface.value,
                    onValueChange = {
                        securiteDeSurface.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Directeur de plongée: ")
                TextField(
                    value = directeurDePlongee.value,
                    onValueChange = {
                        directeurDePlongee.value = it
                    }
                )
            }
        }
        Button(onClick = {
            val modifiedJSONObject = UsefulTools.createModifiedJSONObject(
                date.value,
                selectedLocationIndex.intValue.toString(),
                selectedBoatIndex.intValue.toString(),
                selectedMoment.intValue.toString(),
                minPlongeurs.value,
                maxPlongeurs.value,
                selectedLevelIndex.intValue.toString(),
                pilote.value,
                securiteDeSurface.value,
                directeurDePlongee.value
            )

            val apiPostRequest = PostRequest(context);
            apiPostRequest.insertDive(modifiedJSONObject);
        }) {
            Text(
                text = "Enregistrer"
            )
        }
    }

}





