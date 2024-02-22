package com.example.myapplication

import android.content.Context
import android.os.Bundle
<<<<<<< HEAD
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
=======
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
>>>>>>> 24ce2349092665ecabd45dc3087306eebe273819
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getString
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.ResourceBundle
import com.example.myapplication.url.ApiRequest

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val login = remember{ mutableStateOf("") }
                val password = remember{ mutableStateOf("") }
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LoginForm(this, login, password, Modifier)
                }
                val a =  ApiRequest()
                Log.v(a.connectionTest(),a.connectionTest())
            }
        }
    }
}

@Composable
fun LoginForm(context: Context, login: MutableState<String>, password: MutableState<String>, modifier: Modifier) {
    val t = Toast.makeText(
            context,
            stringResource(id = R.string.form_empty),
            Toast.LENGTH_SHORT
        )
    Column {
        Row {
            Text(text = stringResource(id = R.string.login))
            TextField(value = login.value, onValueChange = {
                login.value = it
            })
        }
        Row {
            Text(text = stringResource(id = R.string.password))
            TextField(value = password.value, onValueChange = {
                password.value = it
            })
        }
        Button(onClick = {
            if(login.value == "" || password.value == "")
                t.show()
        }) {
            Text(text = stringResource(id = R.string.send))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val a =  ApiRequest()

        Log.v(a.connectionTest(),a.connectionTest())
    }
}