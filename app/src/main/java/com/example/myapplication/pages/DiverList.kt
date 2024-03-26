package com.example.myapplication.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.Pages
import com.example.myapplication.R
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DiverList(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, divers: JSONArray, details: JSONArray){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
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

@Composable
fun DiverCard(updatePage: (Pages) -> Unit, updateId: (Int) -> Unit, diver: JSONObject, details: JSONObject) {
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
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = diver.getString("prenom") + " " + diver.getString("nom").uppercase(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "  • ",
                    fontWeight = FontWeight.Bold,
                    color = (
                        if(details.getString("actif") == "true") {
                            Color.Green
                        } else {
                            Color.LightGray
                        }
                    ),
                    textAlign = TextAlign.End
                )
                Text(
                    text = (
                        if(details.getString("actif") == "true") {
                            "actif"
                        } else {
                            "inactif"
                        }
                    ),
                    textAlign = TextAlign.End
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
                    updatePage(Pages.DiverModification)
                    updateId(diver.getString("id").toInt())
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "modifier"
                )
            }
        }
    }
}