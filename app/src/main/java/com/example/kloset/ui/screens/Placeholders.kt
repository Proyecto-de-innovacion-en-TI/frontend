package com.example.kloset.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Pantallas que aún no tienen un archivo propio
@Composable
fun ClosetHomeScreen(onAddGarment: () -> Unit, onGarmentClick: (String) -> Unit) = PlaceholderScreen("Closet Home")

@Composable
fun AddGarmentScreen(onSaved: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Add Garment")

@Composable
fun GarmentDetailScreen(garmentId: String, onBack: () -> Unit, onSellThis: () -> Unit) = PlaceholderScreen("Garment Detail")

@Composable
fun SellGarmentScreen(onPublished: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Sell Garment")

@Composable
fun ProfileScreen(onSettingsClick: () -> Unit) = PlaceholderScreen("Profile")

@Composable
fun SettingsScreen(onLogout: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Settings")

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(text = "Pantalla en construcción", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
