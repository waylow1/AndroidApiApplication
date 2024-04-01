package com.example.myapplication.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.myapplication.Pages
import com.example.myapplication.url.PutRequest
import org.json.JSONObject

@Composable
fun DiveModification(data: String, updatePage: (Pages) -> Unit) {
    val diveData = JSONObject(data)
    val focusManager = LocalFocusManager.current
    val id = diveData.getString("id")
    val date = remember { mutableStateOf(diveData.getString("date")) }
    val location = remember { mutableStateOf(diveData.getString("lieu")) }
    val boat = remember { mutableStateOf(diveData.getString("bateau")) }
    val moment = remember { mutableStateOf(diveData.getInt("moment").toString()) }
    val minPlongeurs = remember { mutableStateOf(diveData.getInt("min_plongeurs").toString()) }
    val maxPlongeurs = remember { mutableStateOf(diveData.getInt("max_plongeurs").toString()) }
    val niveau = remember { mutableStateOf(diveData.getInt("niveau").toString()) }
    val pilote = remember { mutableStateOf(diveData.getInt("pilote").toString()) }
    val securiteDeSurface =
        remember { mutableStateOf(diveData.getInt("securite_de_surface").toString()) }
    val directeurDePlongee =
        remember { mutableStateOf(diveData.getInt("directeur_de_plongee").toString()) }
    val context = LocalContext.current

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

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Modification de la plongée",
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
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Lieu: ")
                TextField(
                    value = location.value,
                    onValueChange = {
                        location.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Bateau: ")
                TextField(
                    value = boat.value,
                    onValueChange = {
                        boat.value = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Moment: ")
                TextField(
                    value = moment.value,
                    onValueChange = {
                        moment.value = it
                    }
                )
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
                TextField(
                    value = niveau.value,
                    onValueChange = {
                        niveau.value = it
                    }
                )
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
        Button(
            onClick = {
                focusManager.clearFocus()
                val modifiedJSONObject = createModifiedJSONObject(
                    date.value,
                    location.value,
                    boat.value,
                    moment.value,
                    minPlongeurs.value,
                    maxPlongeurs.value,
                    niveau.value,
                    pilote.value,
                    securiteDeSurface.value,
                    directeurDePlongee.value
                )
                val apiPutRequest = PutRequest(context)
                apiPutRequest.editDiveById(id, modifiedJSONObject)
                updatePage(Pages.DiveList)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
        ) {
            Text(
                text = "Enregistrer"
            )
        }
    }

}
