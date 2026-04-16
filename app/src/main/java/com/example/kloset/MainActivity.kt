package com.example.kloset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kloset.ui.theme.KlosetTheme
import com.kloset.ui.navigation.KlosetNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlosetTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = ViewModel()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val hasOnboarding by authViewModel.hasCompletedOnboarding.collectAsState()

                Scaffold(
                    bottomBar = {
                        // Ocultar BottomBar en auth y onboarding
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        val showBar = currentRoute in listOf(
                            Screen.ClosetHome.route,
                            Screen.OutfitFeed.route,
                            Screen.MarketplaceHome.route,
                            Screen.Profile.route
                        )
                        if (showBar) KlosetBottomBar(navController)
                    }
                ) { padding ->
                    KlosetNavHost(
                        navController           = navController,
                        isLoggedIn              = isLoggedIn,
                        hasCompletedOnboarding  = hasOnboarding
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KlosetTheme {
        Greeting("Android")
    }
}