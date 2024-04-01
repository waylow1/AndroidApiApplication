package com.example.myapplication.Functions

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.bdd.Levels
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class UsefulTools {


    companion object{

        @Composable
        fun DropdownMenuWithState(
            items: List<String>,
            selectedValue: MutableState<Int>,
            expandedState: MutableState<Boolean>,
            onItemSelected: (Int) -> Unit
        ) {
            Box(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = if (selectedValue.value == -1) "Sélectionner une option" else items[selectedValue.value],
                    modifier = Modifier.clickable {
                        expandedState.value = true
                    }
                )
                DropdownMenu(
                    expanded = expandedState.value,
                    onDismissRequest = { expandedState.value = false }
                ) {
                    items.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedValue.value = index
                                expandedState.value = false
                                onItemSelected(index)
                            }
                        )
                    }
                }
            }
        }

        fun parseBoatsJson(jsonString: String): List<Pair<Int, String>> {
            val boatData = mutableListOf<Pair<Int, String>>()

            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val boatJson = jsonArray.getJSONObject(i)
                    val id = boatJson.getInt("id")
                    val libelle = boatJson.getString("libelle")
                    boatData.add(Pair(id, libelle))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return boatData
        }

        fun createModifiedJSONObject(
            date: String,
            location: String,
            boat: String,
            moment: String,
            minPlongeurs: String,
            maxPlongeurs: String,
            niveau: String,
            pilote: String,
            securiteDeSurface: String,
            directeurDePlongee: String
        ): JSONObject {
            val modifiedData = JSONObject()
            modifiedData.put("date", date)
            modifiedData.put("lieu", location)
            modifiedData.put("bateau", boat)
            modifiedData.put("moment", moment.toInt())
            modifiedData.put("min_plongeurs", minPlongeurs.toInt())
            modifiedData.put("max_plongeurs", maxPlongeurs.toInt())
            modifiedData.put("niveau", niveau.toInt())
            modifiedData.put("pilote", pilote.toInt())
            modifiedData.put("securite_de_surface", securiteDeSurface.toInt())
            modifiedData.put("directeur_de_plongee", directeurDePlongee.toInt())
            return modifiedData
        }

        fun parseLocationsJson(locationsJson: String): List<Pair<Int, String>> {
            val locations = mutableListOf<Pair<Int, String>>()
            locationsJson?.let { jsonString ->
                try {
                    val jsonArray = JSONArray(jsonString)
                    for (i in 0 until jsonArray.length()) {
                        val locationObject = jsonArray.getJSONObject(i)
                        val id = locationObject.getInt("id")
                        val libelle = locationObject.getString("libelle")
                        locations.add(Pair(id, libelle))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return locations
        }

        fun parseJsonToLevels(json: String): List<Levels> {
            val levelsList = mutableListOf<Levels>()

            try {
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("libelle")
                    val level = Levels(id, name)
                    levelsList.add(level)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return levelsList
        }

    }
}