package com.example.kloset.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// ── Modelos de datos ──────────────────────────────────────────────────────

data class ColorOption(
    val id: String,
    val label: String,
    val color: Color
)

val skinTones = listOf(
    ColorOption("very_light",  "Muy claro",   Color(0xFFF5D5B8)),
    ColorOption("light",       "Claro",        Color(0xFFEABF96)),
    ColorOption("medium",      "Medio",        Color(0xFFD4956A)),
    ColorOption("olive",       "Oliva",        Color(0xFFC07D4E)),
    ColorOption("tan",         "Canela",       Color(0xFFA0622A)),
    ColorOption("dark",        "Oscuro",       Color(0xFF7A4010)),
    ColorOption("very_dark",   "Muy oscuro",   Color(0xFF4A2008)),
)

val eyeColors = listOf(
    ColorOption("black",       "Negro",        Color(0xFF1A1A1A)),
    ColorOption("dark_brown",  "Café oscuro",  Color(0xFF3D1F00)),
    ColorOption("brown",       "Café",         Color(0xFF6B3A1F)),
    ColorOption("hazel",       "Miel",         Color(0xFF8B6914)),
    ColorOption("green",       "Verde",        Color(0xFF4A7C4E)),
    ColorOption("blue",        "Azul",         Color(0xFF4A7AAF)),
    ColorOption("gray",        "Gris",         Color(0xFF7A8A9A)),
)

val hairColors = listOf(
    ColorOption("black",       "Negro",        Color(0xFF1A1A1A)),
    ColorOption("dark_brown",  "Café oscuro",  Color(0xFF3D2010)),
    ColorOption("brown",       "Café",         Color(0xFF6B3A1F)),
    ColorOption("light_brown", "Café claro",   Color(0xFF9B6030)),
    ColorOption("blonde",      "Rubio",        Color(0xFFD4A855)),
    ColorOption("red",         "Pelirrojo",    Color(0xFFA03820)),
    ColorOption("gray",        "Gris/Canas",   Color(0xFFA0A0A0)),
    ColorOption("white",       "Blanco",       Color(0xFFE8E8E8)),
)

// ── Pantalla principal ────────────────────────────────────────────────────

@Composable
fun ColorimetryScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var selectedSkin  by remember { mutableStateOf<String?>(null) }
    var selectedEyes  by remember { mutableStateOf<String?>(null) }
    var selectedHair  by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val canContinue = selectedSkin != null && selectedEyes != null && selectedHair != null

    // Paleta sugerida según selección
    val suggestedPalette = remember(selectedSkin, selectedEyes, selectedHair) {
        getSuggestedSeason(selectedSkin, selectedEyes, selectedHair)
    }

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
            Spacer(Modifier.height(56.dp))

            // ── Header ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint               = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Tu paleta de colores",
                style     = MaterialTheme.typography.headlineMedium,
                color     = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Selecciona tus tonos naturales para\nrecomendarte los colores que más te favorecen",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ── Sección tono de piel ──────────────────────────────────
            ColorSection(
                title       = "Tono de piel",
                options     = skinTones,
                selected    = selectedSkin,
                onSelect    = { selectedSkin = it },
                circleSize  = 44
            )

            Spacer(Modifier.height(28.dp))

            // ── Sección color de ojos ─────────────────────────────────
            ColorSection(
                title       = "Color de ojos",
                options     = eyeColors,
                selected    = selectedEyes,
                onSelect    = { selectedEyes = it },
                circleSize  = 40
            )

            Spacer(Modifier.height(28.dp))

            // ── Sección color de cabello ──────────────────────────────
            ColorSection(
                title       = "Color de cabello",
                options     = hairColors,
                selected    = selectedHair,
                onSelect    = { selectedHair = it },
                circleSize  = 40
            )

            Spacer(Modifier.height(28.dp))

            // ── Paleta sugerida (aparece cuando todo está seleccionado)
            // Aquí usamos this@Column.AnimatedVisibility para ser explícitos dentro de Column
            this@Column.AnimatedVisibility(visible = canContinue && suggestedPalette != null) {
                suggestedPalette?.let { palette ->
                    SeasonPaletteCard(palette = palette)
                    Spacer(Modifier.height(24.dp))
                }
            }

            // ── Botón continuar ───────────────────────────────────────
            Button(
                onClick  = { if (canContinue) onNext() },
                enabled  = canContinue,
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

// ── Componente sección de colores ─────────────────────────────────────────

@Composable
private fun ColorSection(
    title: String,
    options: List<ColorOption>,
    selected: String?,
    onSelect: (String) -> Unit,
    circleSize: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            // Aquí estamos dentro de un Row, usamos la AnimatedVisibility estándar o del Row
            AnimatedVisibility(visible = selected != null) {
                val selectedLabel = options.find { it.id == selected }?.label ?: ""
                Text(
                    text  = selectedLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Grid de círculos de color
        val rows = options.chunked(4)
        rows.forEach { rowItems ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { option ->
                    val isSelected = selected == option.id
                    ColorCircle(
                        option     = option,
                        isSelected = isSelected,
                        size       = circleSize,
                        onClick    = { onSelect(option.id) },
                        modifier   = Modifier.weight(1f)
                    )
                }
                // Rellena espacios vacíos si la fila no está completa
                repeat(4 - rowItems.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

// ── Círculo de color individual ───────────────────────────────────────────

@Composable
private fun ColorCircle(
    option: ColorOption,
    isSelected: Boolean,
    size: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(option.color)
                .then(
                    if (isSelected) Modifier.border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ) else Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // SOLUCIÓN: Usar el receptor explícito de la Column externa
            // para que no haya ambigüedad con el BoxScope actual.
            this@Column.AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector        = Icons.Outlined.Check,
                    contentDescription = null,
                    tint               = if (option.color.luminance() > 0.4f)
                        Color.Black else Color.White,
                    modifier           = Modifier.size((size * 0.45f).dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text      = option.label,
            style     = MaterialTheme.typography.labelSmall,
            color     = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines  = 1
        )
    }
}

// ── Card de paleta sugerida ───────────────────────────────────────────────

data class SeasonPalette(
    val season: String,
    val description: String,
    val colors: List<Color>
)

@Composable
private fun SeasonPaletteCard(palette: SeasonPalette) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text  = "Tu estación: ${palette.season}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = palette.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            // Muestra los colores sugeridos
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                palette.colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .background(color)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
            }
        }
    }
}

// ── Lógica de estación sugerida ───────────────────────────────────────────

private fun getSuggestedSeason(
    skin: String?,
    eyes: String?,
    hair: String?
): SeasonPalette? {
    if (skin == null || eyes == null || hair == null) return null

    return when {
        // Invierno — tonos fríos y profundos
        skin in listOf("very_light", "light") &&
                eyes in listOf("black", "dark_brown") &&
                hair in listOf("black", "dark_brown") -> SeasonPalette(
            season      = "Invierno ❄️",
            description = "Te favorecen colores intensos y fríos: negro, blanco puro, azul eléctrico y rojo vino.",
            colors      = listOf(Color(0xFF1A1A2E), Color(0xFFE8E8F0), Color(0xFF2A4A8A), Color(0xFF6A1A2A), Color(0xFF4A0A6A))
        )
        // Verano — tonos fríos y suaves
        skin in listOf("very_light", "light") &&
                eyes in listOf("blue", "gray", "green") -> SeasonPalette(
            season      = "Verano 🌸",
            description = "Te favorecen colores suaves y fríos: malva, lavanda, azul polvoso y rosa palo.",
            colors      = listOf(Color(0xFFB8A0C8), Color(0xFFA0B8C8), Color(0xFFC8A0B0), Color(0xFF90A8B8), Color(0xFFD0B8C8))
        )
        // Otoño — tonos cálidos y profundos
        skin in listOf("medium", "olive", "tan") &&
                hair in listOf("brown", "light_brown", "red") -> SeasonPalette(
            season      = "Otoño 🍂",
            description = "Te favorecen colores cálidos y terrosos: terracota, mostaza, verde oliva y café.",
            colors      = listOf(Color(0xFFD4603A), Color(0xFFD4A830), Color(0xFF6A7A30), Color(0xFF8B4A20), Color(0xFF5A3A20))
        )
        // Primavera — tonos cálidos y claros
        skin in listOf("very_light", "light", "medium") &&
                eyes in listOf("hazel", "green") &&
                hair in listOf("blonde", "light_brown") -> SeasonPalette(
            season      = "Primavera 🌷",
            description = "Te favorecen colores cálidos y brillantes: coral, melocotón, verde lima y dorado.",
            colors      = listOf(Color(0xFFE87050), Color(0xFFE8A870), Color(0xFF90B840), Color(0xFFE8C840), Color(0xFFE88070))
        )
        // Default para tonos más oscuros
        skin in listOf("dark", "very_dark") -> SeasonPalette(
            season      = "Invierno Profundo 🌙",
            description = "Te favorecen colores vivos y contrastantes: blanco brillante, naranja, fucsia y dorado.",
            colors      = listOf(Color(0xFFF0F0F0), Color(0xFFE86020), Color(0xFFD02880), Color(0xFFD4A000), Color(0xFF2050A0))
        )
        else -> SeasonPalette(
            season      = "Otoño Cálido 🌿",
            description = "Te favorecen colores cálidos y neutros: beige, camel, verde salvia y tostado.",
            colors      = listOf(Color(0xFFD4B896), Color(0xFFC09060), Color(0xFF8FAF8A), Color(0xFFB88040), Color(0xFF907060))
        )
    }
}

// Extensión para calcular luminancia de un color
private fun Color.luminance(): Float {
    return 0.2126f * red + 0.7152f * green + 0.0722f * blue
}
