package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Pages
import com.example.myapplication.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationDrawer(
    updatePage: (Pages) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier
            .pointerInteropFilter { return@pointerInteropFilter false },
        drawerState = drawerState,
        drawerContent = {
            Column (
                modifier = Modifier
                    .background(color = Color.DarkGray)
                    .padding(16.dp)
                    .fillMaxHeight()
            )
            {
                Row {
                    Text(
                        text = "Océandroid",
                        modifier = Modifier
                            .padding(2.dp),
                        fontSize = 30.sp
                    )
                    Button(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                            contentDescription = "close"
                        )
                    }
                }
                Text(
                    text = "Liste des plongeurs",
                    modifier = Modifier
                        .clickable {
                            updatePage(Pages.DiverList)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                        .padding(top = 4.dp),
                    fontSize = 18.sp
                )
                Text(
                    text = "Liste des plongées",
                    modifier = Modifier
                        .clickable {
                            updatePage(Pages.DiveList)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                        .padding(top = 4.dp),
                    fontSize = 18.sp
                )
                Text(
                    text = "Palanquées",
                    modifier = Modifier
                        .clickable {
                            /* j'ai rien compris au palanquées */
                            scope.launch {
                                drawerState.close()
                            }
                        }
                        .padding(top = 4.dp),
                    fontSize = 18.sp
                )
                Text(
                    text = "Fiche de sécurité",
                    modifier = Modifier
                        .clickable {
                            updatePage(Pages.SecuritySheet)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                        .padding(top = 4.dp),
                    fontSize = 18.sp
                )
            }
        },
        content = {
            Button(onClick = {

                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription = "open")
            }
        }
    )
}