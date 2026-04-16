package com.example.kloset.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    // Auth
    object Login       : Screen("login")
    object Register    : Screen("register")

    // Onboarding
    object BodyType    : Screen("onboarding/body_type")
    object Colorimetry : Screen("onboarding/colorimetry")
    object Permissions : Screen("onboarding/permissions")

    // Closet
    object ClosetHome  : Screen("closet")
    object AddGarment  : Screen("closet/add")
    object GarmentDetail : Screen("closet/detail/{garmentId}") {
        fun createRoute(garmentId: String) = "closet/detail/$garmentId"
    }

    // Outfits
    object OutfitFeed    : Screen("outfits")
    object OutfitDetail  : Screen("outfits/detail/{outfitId}") {
        fun createRoute(outfitId: String) = "outfits/detail/$outfitId"
    }
    object SavedOutfits  : Screen("outfits/saved")

    // Marketplace
    object MarketplaceHome : Screen("marketplace")
    object ProductDetail   : Screen("marketplace/product/{productId}") {
        fun createRoute(productId: String) = "marketplace/product/$productId"
    }
    object SellGarment     : Screen("marketplace/sell")

    // Perfil
    object Profile  : Screen("profile")
    object Settings : Screen("profile/settings")
}