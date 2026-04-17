package com.example.kloset.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val KlosetShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // tooltips, snackbars
    small      = RoundedCornerShape(8.dp),   // chips, badges, inputs
    medium     = RoundedCornerShape(16.dp),  // cards de prendas
    large      = RoundedCornerShape(24.dp),  // bottom sheets, modales
    extraLarge = RoundedCornerShape(32.dp),  // FAB extendido
)

