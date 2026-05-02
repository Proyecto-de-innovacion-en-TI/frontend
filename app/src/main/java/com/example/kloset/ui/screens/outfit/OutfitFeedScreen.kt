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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale
import com.example.kloset.ui.screens.market.MarketplaceItem
import com.example.kloset.ui.screens.market.sampleMarketplaceItems

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
            sampleMarketplaceItems[0], // Blazer lino beige
            sampleMarketplaceItems[1], // Jeans wide leg
            sampleMarketplaceItems[4]  // Top de seda blanco
        ),
        author = "Stylist AI",
        likes = 124
    ),
    OutfitRecommendation(
        id = "o2",
        title = "Noche de Gala Floral",
        description = "Ideal para eventos semiformales con un toque fresco.",
        items = listOf(
            sampleMarketplaceItems[2], // Vestido floral mid
            sampleMarketplaceItems[3]  // Chaqueta de cuero
        ),
        author = "Trendsetter",
        likes = 89
    ),
    OutfitRecommendation(
        id = "o3",
        title = "Streetwear Utilitario",
        description = "Comodidad y estilo con un toque de tendencia cargo.",
        items = listOf(
            sampleMarketplaceItems[7], // Pantalón cargo caqui
            sampleMarketplaceItems[6]  // Cardigan oversize crema
        ),
        author = "Kloset Team",
        likes = 215
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitFeedScreen(
    onOutfitClick: (String) -> Unit,
    onSavedOutfits: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendaciones", fontWeight = FontWeight.Bold) },
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
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Inspiración para tu estilo",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
            }

            items(sampleOutfits) { outfit ->
                OutfitCard(
                    outfit = outfit,
                    onClick = { onOutfitClick(outfit.id) }
                )
            }
        }
    }
}

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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            // Grid de imágenes de los items
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    outfit.items.take(2).forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(item.colorHex).copy(alpha = 0.8f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                             Text(
                                text = item.category,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                        if (index == 0 && outfit.items.size > 1) {
                            Spacer(Modifier.width(2.dp).fillMaxHeight().background(Color.White))
                        }
                    }
                }

                // Botón de Like
                IconButton(
                    onClick = { isLiked = !isLiked },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.Black
                    )
                }
            }

            // Información del Outfit
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = outfit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Por ${outfit.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = outfit.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(Modifier.height(12.dp))

                // Items list
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    outfit.items.take(3).forEach { item ->
                        Surface(
                            shape = CircleShape,
                            color = Color(item.colorHex).copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(item.colorHex))
                        ) {}
                    }
                    if (outfit.items.size > 3) {
                        Text(
                            text = "+${outfit.items.size - 3}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    
                    Spacer(Modifier.weight(1f))
                    
                    val totalPrice = outfit.items.sumOf { 
                        it.price.replace(".", "").toIntOrNull() ?: 0 
                    }
                    
                    Text(
                        text = "Total: $${String.format(Locale.getDefault(), "%,d", totalPrice).replace(",", ".")}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Comprar este look")
                }
            }
        }
    }
}
