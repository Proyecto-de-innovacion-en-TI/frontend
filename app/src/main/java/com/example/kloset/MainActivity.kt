package com.example.kloset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kloset.ui.navigation.KlosetBottomBar
import com.example.kloset.ui.navigation.KlosetNavHost
import com.example.kloset.ui.navigation.Screen
import com.example.kloset.ui.theme.KlosetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlosetTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Screen.ClosetHome.route,
                    Screen.OutfitFeed.route,
                    Screen.MarketplaceHome.route,
                    Screen.Profile.route
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) KlosetBottomBar(navController)
                    }
                ) { padding ->
                    KlosetNavHost(
                        navController           = navController,
                        isLoggedIn              = true,   // hardcodeado por ahora
                        hasCompletedOnboarding  = true    // hardcodeado por ahora
                    )
                }
            }
        }
    }
}