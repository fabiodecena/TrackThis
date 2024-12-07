package com.example.trackthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.trackthis.ui.theme.TrackThisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackThisTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                ) {
                    TrackApp()
                }
            }
        }
    }
}



