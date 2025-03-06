package com.example.testing2fire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.testing2fire.core.navigation.MainNavigationGraph
import com.example.testing2fire.ui.theme.Testing2fireTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testing2fireTheme {
                // Create NavController
                val navController = rememberNavController()
                MainNavigationGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                        .background(Color(0xFF07203C))
                )
            }
        }
    }
}

