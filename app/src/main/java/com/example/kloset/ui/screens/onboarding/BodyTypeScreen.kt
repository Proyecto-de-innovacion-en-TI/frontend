package com.example.kloset.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas

data class BodyType(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val traits: List<String>
)

val bodyTypes = listOf(
    BodyType(
        id          = "ectomorph",
        name        = "Ectomorfo",
        subtitle    = "Complexión delgada",
        description = "Estructura ósea pequeña, metabolismo rápido y tendencia a ser delgado naturalmente.",
        traits      = listOf("Hombros estrechos", "Poca grasa corporal", "Difícil ganar masa")
    ),
    BodyType(
        id          = "mesomorph",
        name        = "Mesomorfo",
        subtitle    = "Complexión atlética",
        description = "Complexión atlética y musculosa, con facilidad para ganar músculo y perder grasa.",
        traits      = listOf("Hombros anchos", "Cintura definida", "Musculatura visible")
    ),
    BodyType(
        id          = "endomorph",
        name        = "Endomorfo",
        subtitle    = "Complexión robusta",
        description = "Estructura ósea grande, tendencia a acumular grasa y mayor volumen corporal.",
        traits      = listOf("Caderas anchas", "Metabolismo lento", "Cuerpo redondeado")
    )
)

@Composable
fun BodyTypeScreen(onNext: () -> Unit) {
    var selected by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val primary         = MaterialTheme.colorScheme.primary
    val surfaceVariant  = MaterialTheme.colorScheme.surfaceVariant
    val onSurface       = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // ── Header ────────────────────────────────────────────────
            Text(
                text  = "¿Cuál es tu tipo\nde cuerpo?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Esto nos ayuda a recomendarte outfits\nque resalten tu figura",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // ── Cards de tipo de cuerpo ───────────────────────────────
            bodyTypes.forEach { bodyType ->
                val isSelected = selected == bodyType.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { selected = bodyType.id }
                        .then(
                            if (isSelected) Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            ) else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ilustración SVG con Canvas
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surface
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            BodyTypeIllustration(
                                bodyTypeId = bodyType.id,
                                color      = if (isSelected) primary else onSurface.copy(alpha = 0.4f),
                                modifier   = Modifier.size(48.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text  = bodyType.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text  = bodyType.subtitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text  = bodyType.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Checkmark animado
                        AnimatedVisibility(visible = isSelected) {
                            Icon(
                                imageVector        = Icons.Outlined.ArrowForward,
                                contentDescription = null,
                                tint               = MaterialTheme.colorScheme.primary,
                                modifier           = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    // Traits expandidos al seleccionar
                    AnimatedVisibility(visible = isSelected) {
                        Row(
                            modifier              = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            bodyType.traits.forEach { trait ->
                                SuggestionChip(
                                    onClick = {},
                                    label   = {
                                        Text(
                                            text  = trait,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))

            // ── Botón continuar ───────────────────────────────────────
            Button(
                onClick  = { if (selected != null) onNext() },
                enabled  = selected != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text  = "Continuar",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector        = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier           = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Saltar por ahora ──────────────────────────────────────
            TextButton(onClick = onNext) {
                Text(
                    text  = "Saltar por ahora",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Ilustraciones con Canvas ──────────────────────────────────────────────
@Composable
private fun BodyTypeIllustration(
    bodyTypeId: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        when (bodyTypeId) {
            "ectomorph" -> drawEctomorph(color)
            "mesomorph" -> drawMesomorph(color)
            "endomorph" -> drawEndomorph(color)
        }
    }
}

// Ectomorfo — figura delgada y alargada
private fun DrawScope.drawEctomorph(color: Color) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.42f, h * 0.05f)
        cubicTo(w * 0.58f, h * 0.05f, w * 0.60f, h * 0.18f, w * 0.58f, h * 0.25f)
        cubicTo(w * 0.62f, h * 0.30f, w * 0.63f, h * 0.45f, w * 0.58f, h * 0.52f)
        cubicTo(w * 0.60f, h * 0.65f, w * 0.62f, h * 0.80f, w * 0.60f, h * 0.95f)
        lineTo(w * 0.40f, h * 0.95f)
        cubicTo(w * 0.38f, h * 0.80f, w * 0.40f, h * 0.65f, w * 0.42f, h * 0.52f)
        cubicTo(w * 0.37f, h * 0.45f, w * 0.38f, h * 0.30f, w * 0.42f, h * 0.25f)
        cubicTo(w * 0.40f, h * 0.18f, w * 0.42f, h * 0.05f, w * 0.42f, h * 0.05f)
        close()
    }
    drawPath(path, color, style = Fill)

    // Cabeza
    drawCircle(color, radius = w * 0.10f, center = androidx.compose.ui.geometry.Offset(w * 0.50f, h * 0.06f))
}

// Mesomorfo — figura atlética con hombros anchos y cintura definida
private fun DrawScope.drawMesomorph(color: Color) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.30f, h * 0.20f)
        cubicTo(w * 0.50f, h * 0.15f, w * 0.70f, h * 0.20f, w * 0.70f, h * 0.20f)
        cubicTo(w * 0.72f, h * 0.35f, w * 0.65f, h * 0.42f, w * 0.58f, h * 0.48f)
        cubicTo(w * 0.65f, h * 0.60f, w * 0.66f, h * 0.75f, w * 0.64f, h * 0.95f)
        lineTo(w * 0.36f, h * 0.95f)
        cubicTo(w * 0.34f, h * 0.75f, w * 0.35f, h * 0.60f, w * 0.42f, h * 0.48f)
        cubicTo(w * 0.35f, h * 0.42f, w * 0.28f, h * 0.35f, w * 0.30f, h * 0.20f)
        close()
    }
    drawPath(path, color, style = Fill)
    drawCircle(color, radius = w * 0.11f, center = androidx.compose.ui.geometry.Offset(w * 0.50f, h * 0.09f))
}

// Endomorfo — figura más redondeada y ancha
private fun DrawScope.drawEndomorph(color: Color) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.35f, h * 0.20f)
        cubicTo(w * 0.50f, h * 0.14f, w * 0.65f, h * 0.20f, w * 0.68f, h * 0.28f)
        cubicTo(w * 0.78f, h * 0.38f, w * 0.76f, h * 0.52f, w * 0.70f, h * 0.58f)
        cubicTo(w * 0.74f, h * 0.70f, w * 0.72f, h * 0.85f, w * 0.68f, h * 0.95f)
        lineTo(w * 0.32f, h * 0.95f)
        cubicTo(w * 0.28f, h * 0.85f, w * 0.26f, h * 0.70f, w * 0.30f, h * 0.58f)
        cubicTo(w * 0.24f, h * 0.52f, w * 0.22f, h * 0.38f, w * 0.32f, h * 0.28f)
        cubicTo(w * 0.33f, h * 0.23f, w * 0.35f, h * 0.20f, w * 0.35f, h * 0.20f)
        close()
    }
    drawPath(path, color, style = Fill)
    drawCircle(color, radius = w * 0.12f, center = androidx.compose.ui.geometry.Offset(w * 0.50f, h * 0.09f))
}

