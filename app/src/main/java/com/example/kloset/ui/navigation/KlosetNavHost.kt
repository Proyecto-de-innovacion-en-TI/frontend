// navigation/KlosetNavHost.kt
package com.example.kloset.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kloset.ui.screens.auth.LoginScreen
import com.example.kloset.ui.screens.auth.RegisterScreen
import com.example.kloset.ui.screens.onboarding.BodyTypeScreen
import com.example.kloset.ui.screens.*

@Composable
fun KlosetNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,           // viene del AuthViewModel
    hasCompletedOnboarding: Boolean
) {
    // Punto de entrada dinámico
    val startDestination = when {
        !isLoggedIn             -> Screen.Login.route
        !hasCompletedOnboarding -> Screen.BodyType.route
        else                    -> Screen.ClosetHome.route
    }

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        // ── AUTH ────────────────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess  = { navController.navigate(Screen.ClosetHome.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }},
                onGoToRegister  = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistered = { navController.navigate(Screen.BodyType.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }},
                onBack = { navController.popBackStack() }
            )
        }

        // ── ONBOARDING ──────────────────────────────────────────────────
        composable(Screen.BodyType.route) {
            BodyTypeScreen(
                onNext = { navController.navigate(Screen.Colorimetry.route) }
            )
        }

        composable(Screen.Colorimetry.route) {
            ColorimetryScreen(
                onNext = { navController.navigate(Screen.Permissions.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Permissions.route) {
            PermissionsScreen(
                onFinish = { navController.navigate(Screen.ClosetHome.route) {
                    popUpTo(Screen.BodyType.route) { inclusive = true }
                }}
            )
        }

        // ── CLOSET ──────────────────────────────────────────────────────
        composable(Screen.ClosetHome.route) {
            ClosetHomeScreen(
                onAddGarment     = { navController.navigate(Screen.AddGarment.route) },
                onGarmentClick   = { id -> navController.navigate(Screen.GarmentDetail.createRoute(id)) }
            )
        }

        composable(Screen.AddGarment.route) {
            AddGarmentScreen(
                onSaved = { navController.popBackStack() },
                onBack  = { navController.popBackStack() }
            )
        }

        composable(
            route     = Screen.GarmentDetail.route,
            arguments = listOf(navArgument("garmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val garmentId = backStackEntry.arguments?.getString("garmentId") ?: return@composable
            GarmentDetailScreen(
                garmentId  = garmentId,
                onBack     = { navController.popBackStack() },
                onSellThis = { navController.navigate(Screen.SellGarment.route) }
            )
        }

        // ── OUTFITS ─────────────────────────────────────────────────────
        composable(Screen.OutfitFeed.route) {
            OutfitFeedScreen(
                onOutfitClick  = { id -> navController.navigate(Screen.OutfitDetail.createRoute(id)) },
                onSavedOutfits = { navController.navigate(Screen.SavedOutfits.route) }
            )
        }

        composable(
            route     = Screen.OutfitDetail.route,
            arguments = listOf(navArgument("outfitId") { type = NavType.StringType })
        ) { backStackEntry ->
            val outfitId = backStackEntry.arguments?.getString("outfitId") ?: return@composable
            OutfitDetailScreen(
                outfitId = outfitId,
                onBack   = { navController.popBackStack() }
            )
        }

        composable(Screen.SavedOutfits.route) {
            SavedOutfitsScreen(
                onOutfitClick = { id -> navController.navigate(Screen.OutfitDetail.createRoute(id)) },
                onBack        = { navController.popBackStack() }
            )
        }

        // ── MARKETPLACE ─────────────────────────────────────────────────
        composable(Screen.MarketplaceHome.route) {
            MarketplaceHomeScreen(
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onSellClick    = { navController.navigate(Screen.SellGarment.route) }
            )
        }

        composable(
            route     = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            ProductDetailScreen(
                productId = productId,
                onBack    = { navController.popBackStack() }
            )
        }

        composable(Screen.SellGarment.route) {
            SellGarmentScreen(
                onPublished = { navController.popBackStack() },
                onBack      = { navController.popBackStack() }
            )
        }

        // ── PERFIL ──────────────────────────────────────────────────────
        composable(Screen.Profile.route) {
            ProfileScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = { navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }},
                onBack = { navController.popBackStack() }
            )
        }
    }
}


