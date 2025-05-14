package com.dindaka.distancemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.dindaka.distancemap.ui.navigation.AppNavigation
import com.dindaka.distancemap.ui.theme.DistanceMapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DistanceMapTheme {
                val navigationController = rememberNavController()
                AppNavigation(navigationController)
            }
        }
    }
}