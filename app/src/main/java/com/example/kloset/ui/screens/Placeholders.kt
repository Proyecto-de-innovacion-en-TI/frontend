package com.example.kloset.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ColorimetryScreen(onNext: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Colorimetry")

@Composable
fun PermissionsScreen(onFinish: () -> Unit) = PlaceholderScreen("Permissions")

@Composable
fun ClosetHomeScreen(onAddGarment: () -> Unit, onGarmentClick: (String) -> Unit) = PlaceholderScreen("Closet Home")

@Composable
fun AddGarmentScreen(onSaved: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Add Garment")

@Composable
fun GarmentDetailScreen(garmentId: String, onBack: () -> Unit, onSellThis: () -> Unit) = PlaceholderScreen("Garment Detail")

@Composable
fun OutfitFeedScreen(onOutfitClick: (String) -> Unit, onSavedOutfits: () -> Unit) = PlaceholderScreen("Outfit Feed")

@Composable
fun OutfitDetailScreen(outfitId: String, onBack: () -> Unit) = PlaceholderScreen("Outfit Detail")

@Composable
fun SavedOutfitsScreen(onOutfitClick: (String) -> Unit, onBack: () -> Unit) = PlaceholderScreen("Saved Outfits")

@Composable
fun MarketplaceHomeScreen(onProductClick: (String) -> Unit, onSellClick: () -> Unit) = PlaceholderScreen("Marketplace")

@Composable
fun ProductDetailScreen(productId: String, onBack: () -> Unit) = PlaceholderScreen("Product Detail")

@Composable
fun SellGarmentScreen(onPublished: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Sell Garment")

@Composable
fun ProfileScreen(onSettingsClick: () -> Unit) = PlaceholderScreen("Profile")

@Composable
fun SettingsScreen(onLogout: () -> Unit, onBack: () -> Unit) = PlaceholderScreen("Settings")

// ── Helper ──────────────────────────────────────────────────────────────────
@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text  = name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "Pantalla en construcción",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}