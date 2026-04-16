// ui/theme/Type.kt
package com.kloset.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.kloset.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

val playfairDisplay = FontFamily(
    Font(googleFont = GoogleFont("Playfair Display"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Playfair Display"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Playfair Display"), fontProvider = provider, weight = FontWeight.Bold),
)

val dmSans = FontFamily(
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = provider, weight = FontWeight.SemiBold),
)

val KlosetTypography = Typography(
    // Títulos grandes — Playfair para impacto editorial
    displayLarge  = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.Bold,   fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.Bold,   fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall  = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.SemiBold, fontSize = 36.sp, lineHeight = 44.sp),

    headlineLarge  = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall  = TextStyle(fontFamily = playfairDisplay, fontWeight = FontWeight.Normal,   fontSize = 24.sp, lineHeight = 32.sp),

    // UI y body — DM Sans para legibilidad
    titleLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 16.sp, lineHeight = 24.sp),
    titleSmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),

    bodyLarge   = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall   = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),

    labelLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp),
)