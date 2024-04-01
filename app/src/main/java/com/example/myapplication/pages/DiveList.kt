package com.example.myapplication.pages

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
fun DiveList(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, dives: JSONArray, sites: JSONArray){

    Scaffold(
        floatingActionButton = {
            AddDiveButton(updatePage = updatePage)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                for( i in 0 until dives.length()){
                    var siteIdx = 0
                    val dive = dives.getJSONObject(i)
                    for( j in 0 until sites.length()){
                        if(sites.getJSONObject(j).getString("id") == dive.getString("lieu")) {
                            siteIdx = j
                            DiveCard(
                                updatePage = updatePage,
                                updateId = updateId,
                                dive = dives.getJSONObject(i),
                                site = sites.getJSONObject(siteIdx)
                            )
                            break
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DiveCard(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, dive: JSONObject, site: JSONObject) {
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
                    text = dive.getString("date"),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Row {
                    Text(
                        text = "  • ",
                        fontWeight = FontWeight.Bold,
                        color = (
                                if(dive.getString("active") == "true") {
                                    Color.Green
                                } else {
                                    Color.LightGray
                                }
                                ),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = (
                                if(dive.getString("active") == "true") {
                                    "active"
                                } else {
                                    "inactive"
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
                            delete.deleteDiveById(dive.getString("id"))
                        },
                    tint = Color.LightGray
                )
            }
            Text(
                text = site.getString("libelle"),
                modifier = Modifier
                    .padding(start = 16.dp),
                color = Color.Gray,
                textAlign = TextAlign.Left
            )
            Button(
                onClick = {
                    updateId(dive.getString("id").toInt())
                    updatePage(Pages.DiveModification)
                },
                modifier = Modifier
                    .padding(16.dp),
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
fun AddDiveButton(updatePage: (Pages) -> Unit) {
    FloatingActionButton(
        onClick = {
            updatePage(Pages.DiveCreation)
        },
        containerColor = Color.Magenta,
        contentColor = Color.White
        ) {
        Icon(
            Icons.Filled.Add,
            "Floating action button.",
            tint = Color.White
        )
    }
}