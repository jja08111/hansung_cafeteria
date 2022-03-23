package com.foundy.hansungcafeteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.startup.AppInitializer
import com.foundy.hansungcafeteria.ui.HansungCafeteria
import net.danlew.android.joda.JodaTimeInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppInitializer.getInstance(this).initializeComponent(JodaTimeInitializer::class.java)
        setContent {
            HansungCafeteria()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HansungCafeteria()
}
