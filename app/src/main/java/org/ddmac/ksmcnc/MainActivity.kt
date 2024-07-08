package org.ddmac.ksmcnc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import org.ddmac.ksmcnc.ui.theme.KsmcncTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: MainActivityViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            KsmcncTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val selected by vm.selected.collectAsStateWithLifecycle()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$selected", fontSize = TextUnit(22f, TextUnitType.Sp))
                        Button(onClick = {
                            sendReq(0)
                        }) {
                            Text(text = "0")
                        }
                        Button(onClick = {
                            sendReq(1)
                        }) {
                            Text(text = "1")
                        }
                    }

                }
            }
        }
    }

    fun sendReq(selection: Int){
        CoroutineScope(Dispatchers.Default).launch {
            val res = OkHttpClient()
                .newCall(
                    Request
                        .Builder()
                        .url("")
                        .post("{\"selected\": $selection}".toRequestBody("application/json".toMediaTypeOrNull()))
                        .build()
                ).execute()

            res.body?.let {
                Log.d(MainActivity::class.simpleName,it.string())
            }
        }
    }
}
