package com.example.kloset.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kloset.ui.screens.market.sampleMarketplaceItems

// ── Pantalla detalle de producto ──────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBack: () -> Unit,
    categoryIcon: (String) -> ImageVector
) {
    // Buscar el item en los datos de muestra; si no se encuentra, usar el primero
    val item = sampleMarketplaceItems.find { it.id == productId }
        ?: sampleMarketplaceItems.first()

    var isFavorited by remember { mutableStateOf(item.isFavorited) }
    var showContactDialog by remember { mutableStateOf(false) }
    var showExchangeDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Datos de vendedor extendidos (quemados)
    val sellerRating = 4.8f
    val sellerSales = 23
    val sellerResponseTime = "< 1 hora"

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // ── Imagen / Hero ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(item.colorHex).copy(alpha = 0.95f),
                                Color(item.colorHex).copy(alpha = 0.65f)
                            )
                        )
                    )
            ) {
                // Ícono central representando la prenda
                Icon(
                    imageVector = categoryIcon(item.category),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                )

                // Badge condición
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart),
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.NewReleases,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = item.condition,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Badge intercambio si aplica
                if (item.isExchange) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.SwapHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "Intercambio",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            // ── Contenido principal ───────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))

                // Título y precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.brand,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "$ ${item.price}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ── Detalles en chips ─────────────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailChip(icon = Icons.Outlined.Straighten, label = "Talla ${item.size}")
                    DetailChip(icon = Icons.Outlined.Category, label = item.category)
                    DetailChip(icon = Icons.Outlined.LocationOn, label = item.sellerCity)
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))

                // ── Perfil del vendedor ───────────────────────────────
                Text(
                    text = "Vendedora",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.sellerName.first().toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.sellerName,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "$sellerRating · $sellerSales ventas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "Responde en $sellerResponseTime",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Icon(
                            Icons.Outlined.Verified,
                            contentDescription = "Verificado",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))

                // ── Info adicional ────────────────────────────────────
                Text(
                    text = "Información",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                InfoRow(icon = Icons.Outlined.LocalShipping, label = "Envío", value = "A convenir con el vendedor")
                Spacer(Modifier.height(8.dp))
                InfoRow(icon = Icons.Outlined.Shield, label = "Pago seguro", value = "Coordinado por Kloset")
                Spacer(Modifier.height(8.dp))
                InfoRow(icon = Icons.Outlined.Autorenew, value = "Si la prenda no es como se describe, te ayudamos", label = "Política")

                // Nota de privacidad
                Spacer(Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Tu número no se comparte. Toda la comunicación se realiza dentro de la app.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Spacer(Modifier.height(100.dp)) // espacio para el botón flotante
            }
        }

        // ── Top Bar flotante ──────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Volver")
                }
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = { isFavorited = !isFavorited }) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorited) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // ── Botones de acción fijos en la parte inferior ──────────────
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showContactDialog = true },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Preguntar")
                }
                Button(
                    onClick = { showContactDialog = true },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Comprar")
                }
            }
        }
    }

    // ── Diálogo de contacto ───────────────────────────────────────────
    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            icon = { Icon(Icons.Outlined.Chat, contentDescription = null) },
            title = { Text("Contactar a ${item.sellerName}") },
            text = {
                Text(
                    text = "Te conectaremos con ${item.sellerName} para coordinar ${if (item.isExchange) "el intercambio" else "la compra"} de \"${item.title}\".\n\nToda la comunicación queda dentro de Kloset.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick = { showContactDialog = false }) {
                    Text("Enviar mensaje")
                }
            },
            dismissButton = {
                TextButton(onClick = { showContactDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ── Diálogo de intercambio ────────────────────────────────────────
    if (showExchangeDialog) {
        AlertDialog(
            onDismissRequest = { showExchangeDialog = false },
            icon = { Icon(Icons.Outlined.SwapHoriz, contentDescription = null) },
            title = { Text("Proponer intercambio") },
            text = {
                Text(
                    text = "Selecciona una prenda de tu armario para proponer el intercambio con \"${item.title}\".\n\n(Esta función estará disponible próximamente.)",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick = { showExchangeDialog = false }) {
                    Text("Ir a mi armario")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExchangeDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ── Componentes auxiliares ────────────────────────────────────────────────

@Composable
private fun DetailChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}