package com.example.myapplication.pages

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.getValue
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

    /* ApiRequest Object */
    val apiRequestAllBoats = GetRequest(context);
    val apiRequestLocations = GetRequest(context);
    val apiRequestDivers = GetRequest(context);

    /* LiveData observer */
    val boats = apiRequestAllBoats.getLiveData().observeAsState();
    val locations = apiRequestLocations.getLiveData().observeAsState();
    val allPersons = apiRequestDivers.getLiveData().observeAsState();

    /* Data rembembers */
    val boatData = remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }
    val locationData = remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }

    val pilotsMap = remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    val securityMap = remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    val directorsMap = remember { mutableStateOf<Map<Int, String>>(emptyMap()) }

    /* Fetching Api */
    apiRequestAllBoats.getAllBoats();
    apiRequestLocations.getAllLocations();
    apiRequestDivers.getAllPersons();

    if (boats.value != null) {
        boatData.value = UsefulTools.parseBoatsJson(boats.value.toString())
    }

    if (locations.value != null) {
        locationData.value = UsefulTools.parseLocationsJson(locations.value.toString())
    }

    if (allPersons.value != null) {
        val personsJsonString = allPersons.value.toString()
        val personsJsonArray = JSONArray(personsJsonString)

        val pilots = mutableMapOf<Int, String>()
        val security = mutableMapOf<Int, String>()
        val directors = mutableMapOf<Int, String>()

        for (i in 0 until personsJsonArray.length()) {
            val personJsonObject = personsJsonArray.getJSONObject(i)
            val id = personJsonObject.getInt("id")
            val nom = personJsonObject.getString("nom")
            if (personJsonObject.optBoolean("pilote", false)) {
                pilots[id] = nom
            }
            if (personJsonObject.optBoolean("securite_de_surface", false)) {
                security[id] = nom
            }
            if (personJsonObject.optBoolean("directeur_de_section", false)) {
                directors[id] = nom
            }
        }
        pilotsMap.value = pilots
        securityMap.value = security
        directorsMap.value = directors

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

    val selectedPilotIndex = remember {
        mutableIntStateOf(-1)
    }

    val selectedSecurityIndex = remember {
        mutableIntStateOf(-1)
    }

    val selectedDirectorIndex = remember {
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

    val expandedPilot = remember {
        mutableStateOf(false)
    }

    val expandedSecurity = remember {
        mutableStateOf(false)
    }

    val expandedDirector = remember {
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
                    }, placeholder = { Text(text = "Format: 2022-02-26") }
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
                ) {
                    Log.v("Moment", selectedMoment.intValue.toString())
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
                ) {
                    Log.v("Level", levels.value[selectedLevelIndex.intValue].name)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pilote: ")
                UsefulTools.DropdownMenuWithState(
                    items = pilotsMap.value.toList().map { it.second },
                    selectedValue = selectedPilotIndex,
                    expandedState = expandedPilot,
                    onItemSelected = {
                        val selectedId = pilotsMap.value.keys.toList()[selectedPilotIndex.intValue]
                        val selectedNom = pilotsMap.value.values.toList()[selectedPilotIndex.intValue]
                        Log.v("Selected Pilot", "ID: $selectedId, Nom: $selectedNom")
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sécurité de surface: ")
                UsefulTools.DropdownMenuWithState(
                    items = securityMap.value.toList().map { it.second },
                    selectedValue = selectedSecurityIndex,
                    expandedState = expandedSecurity,
                    onItemSelected = {
                        val selectedId =
                            securityMap.value.keys.toList()[selectedSecurityIndex.intValue]
                        val selectedNom =
                            securityMap.value.values.toList()[selectedSecurityIndex.intValue]
                        Log.v("Selected Security", "ID: $selectedId, Nom: $selectedNom")
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Directeur de plongée: ")
                UsefulTools.DropdownMenuWithState(
                    items = directorsMap.value.toList().map { it.second },
                    selectedValue = selectedDirectorIndex,
                    expandedState = expandedDirector,
                    onItemSelected = {
                        val selectedId =
                            directorsMap.value.keys.toList()[selectedDirectorIndex.intValue]
                        val selectedNom =
                            directorsMap.value.values.toList()[selectedDirectorIndex.intValue]
                        Log.v("Selected Director", "ID: $selectedId, Nom: $selectedNom")
                    }
                )
            }
        }
        Button(onClick = {
            if ((minPlongeurs.value.toIntOrNull() ?: 0) >= (maxPlongeurs.value.toIntOrNull()
                    ?: 0)
            ) {
                Toast.makeText(
                    context,
                    "Le nombre minimum de plongeurs doit être inférieur au nombre maximum",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (selectedLocationIndex.intValue == -1 ||
                selectedBoatIndex.intValue == -1 ||
                selectedMoment.intValue == -1 ||
                selectedLevelIndex.intValue == -1 ||
                selectedPilotIndex.intValue == -1 ||
                selectedSecurityIndex.intValue == -1 ||
                selectedDirectorIndex.intValue == -1
            ) {

                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val modifiedJSONObject = UsefulTools.createModifiedJSONObject(
                    date.value,
                    selectedLocationIndex.intValue.toString(),
                    selectedBoatIndex.intValue.toString(),
                    (selectedMoment.intValue+1).toString(),
                    minPlongeurs.value,
                    maxPlongeurs.value,
                    selectedLevelIndex.intValue.toString(),
                    selectedPilotIndex.intValue.toString(),
                    selectedSecurityIndex.intValue.toString(),
                    selectedDirectorIndex.intValue.toString()
                )

                Log.v("esttez",modifiedJSONObject.toString())
                val apiPostRequest = PostRequest(context);
                apiPostRequest.insertDive(modifiedJSONObject);
            }
        }) {
            Text(
                text = "Enregistrer"
            )
        }
    }

}





