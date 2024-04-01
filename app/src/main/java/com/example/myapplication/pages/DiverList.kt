package com.example.myapplication.pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.Pages
import com.example.myapplication.R
import com.example.myapplication.url.DeleteRequest
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DiverList(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, divers: JSONArray, details: JSONArray){
    Scaffold(
        floatingActionButton = {
            AddDiverButton(updatePage = updatePage)
        },
        content = { padding ->
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(padding)
            ) {
                for( i in 0 until divers.length()){
                    var diverDetailIdx = 0
                    val diver = divers.getJSONObject(i)
                    for( j in 0 until details.length()){
                        if(divers.getJSONObject(j).getString("id") == diver.getString("id")) {
                            diverDetailIdx = j
                        }
                    }
                    DiverCard(
                        updatePage = updatePage,
                        updateId = updateId,
                        diver = divers.getJSONObject(i),
                        details.getJSONObject(diverDetailIdx)
                    )
                }
            }
        }
    )
}

@Composable
fun DiverCard(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, diver: JSONObject, details: JSONObject) {
    val context = LocalContext.current
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = diver.getString("prenom") + " " + diver.getString("nom").uppercase(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Row {
                    Text(
                        text = "  • ",
                        fontWeight = FontWeight.Bold,
                        color = (
                                if (details.getString("actif") == "true") {
                                    Color.Green
                                } else {
                                    Color.LightGray
                                }
                                ),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = (
                                if (details.getString("actif") == "true") {
                                    "actif"
                                } else {
                                    "inactif"
                                }
                                ),
                        textAlign = TextAlign.End
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            val delete = DeleteRequest(context);
                            delete.deleteDiversById(diver.getString("id"))
                        },
                    tint = Color.LightGray
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
                    updateId(diver.getString("id").toInt())
                    updatePage(Pages.DiverModification)
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "modifier",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AddDiverButton(updatePage: (Pages) -> Unit) {
    FloatingActionButton(
        onClick = {
            updatePage(Pages.DiverCreation)
        },
        containerColor = Color.Magenta
        ) {
        Icon(
            Icons.Filled.Add,
            "Floating action button.",
            tint = Color.White
        )
    }
}