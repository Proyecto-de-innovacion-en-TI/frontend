package com.example.kloset.ui.screens.outfit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.kloset.ui.screens.market.MarketplaceItem
import com.example.kloset.ui.screens.market.sampleMarketplaceItems
import java.util.Locale

// ── Modelo de Datos ───────────────────────────────────────────────────────

data class OutfitRecommendation(
    val id: String,
    val title: String,
    val description: String,
    val items: List<MarketplaceItem>,
    val author: String,
    val likes: Int
)

val sampleOutfits = listOf(
    OutfitRecommendation(
        id = "o1",
        title = "Casual Chic de Oficina",
        description = "Un look versátil para el día a día combinando tonos neutros.",
        items = listOf(
            sampleMarketplaceItems[0], // Blazer beige
            sampleMarketplaceItems[4], // Top seda
            sampleMarketplaceItems[1]  // Jeans
        ),
        author = "Stylist AI",
        likes = 124
    ),
    OutfitRecommendation(
        id = "o2",
        title = "Noche de Gala Floral",
        description = "Ideal para eventos semiformales con un toque fresco.",
        items = listOf(
            sampleMarketplaceItems[2], // Vestido floral
            sampleMarketplaceItems[3]  // Chaqueta cuero
        ),
        author = "Trendsetter",
        likes = 89
    ),
    OutfitRecommendation(
        id = "o3",
        title = "Streetwear Utilitario",
        description = "Comodidad y estilo con un toque de tendencia cargo.",
        items = listOf(
            sampleMarketplaceItems[7], // Pantalón cargo
            sampleMarketplaceItems[6]  // Cardigan oversize
        ),
        author = "Kloset Team",
        likes = 215
    )
)

// ── Pantalla Principal ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitFeedScreen(
    onOutfitClick: (String) -> Unit,
    onSavedOutfits: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspiración", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSavedOutfits) {
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Guardados")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(sampleOutfits) { outfit ->
                OutfitCard(
                    outfit = outfit,
                    onClick = { onOutfitClick(outfit.id) }
                )
            }
        }
    }
}

// ── Componente de Tarjeta (Mejorado con manejo de Error) ──────────────────

@Composable
fun OutfitCard(
    outfit: OutfitRecommendation,
    onClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column {
            // Mosaico de imágenes
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                Row(modifier = Modifier.fillMaxSize()) {
                    outfit.items.take(2).forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            // Cambiamos AsyncImage por SubcomposeAsyncImage para poder usar el bloque 'error'
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                loading = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    )
                                },
                                error = {
                                    Icon(
                                        imageVector = Icons.Outlined.ImageNotSupported,
                                        contentDescription = "Error al cargar",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(32.dp)
                                    )
                                },
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        if (index == 0 && outfit.items.size > 1) {
                            Spacer(Modifier.width(2.dp).fillMaxHeight().background(Color.White))
                        }
                    }
                }
                
                IconButton(
                    onClick = { isLiked = !isLiked },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(Color.White.copy(0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isLiked) Color.Red else Color.Black
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = outfit.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = outfit.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                
                Spacer(Modifier.height(12.dp))
                
                val totalPrice = outfit.items.sumOf { it.price.replace(".", "").toIntOrNull() ?: 0 }
                Text(
                    text = "Look completo por $${String.format(Locale.getDefault(), "%,d", totalPrice).replace(",", ".")}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
