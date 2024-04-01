package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.Pages
import com.example.myapplication.R


@Composable
fun Navigation(
    page: Pages,
    updatePage: (Pages) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Magenta
            )
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_accessibility_24),
            contentDescription = "Plongeurs",
            modifier = Modifier
                .clickable {
                    updatePage(Pages.DiverList)
                }
                .size(40.dp),
            tint = (
                if(page == Pages.DiverList || page == Pages.DiverCreation || page == Pages.DiverModification) {
                    Color.LightGray
                } else {
                    Color.White
                }
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_anchor_24),
            contentDescription = "Plongeurs",
            modifier = Modifier
                .clickable {
                    updatePage(Pages.DiveList)
                }
                .size(40.dp),
            tint = (
                if(page == Pages.DiveList || page == Pages.DiveCreation || page == Pages.DiveModification) {
                    Color.LightGray
                } else {
                    Color.White
                }
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_bar_chart_24),
            contentDescription = "Plongeurs",
            modifier = Modifier
                .clickable {
                    updatePage(Pages.Stats)
                }
                .size(40.dp),
            tint = (
                if(page == Pages.Stats) {
                    Color.LightGray
                } else {
                    Color.White
                }
            )
        )
    }
}