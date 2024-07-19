package org.example.project

import App
import SetupNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(commonModule())
        }
        setContent {
            val navController = rememberNavController()
            SetupNavGraph(navController = navController)
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}