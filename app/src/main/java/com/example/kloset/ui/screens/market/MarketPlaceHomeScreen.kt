package com.example.kloset.ui.screens.market

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

// ── Modelos de datos ──────────────────────────────────────────────────────

data class MarketplaceItem(
    val id: String,
    val title: String,
    val brand: String,
    val size: String,
    val price: String,
    val isExchange: Boolean,
    val condition: String,
    val category: String,
    val imageUrl: String,
    val sellerName: String,
    val sellerCity: String,
    val isFavorited: Boolean = false
)

val sampleMarketplaceItems = listOf(
    MarketplaceItem(
        id = "1", title = "Blazer lino beige", brand = "Zara",
        size = "M", price = "85.000", isExchange = false,
        condition = "Como nuevo", category = "Tops",
        imageUrl = "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?q=80&w=500&auto=format&fit=crop",
        sellerName = "Valentina R.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "2", title = "Jeans wide leg", brand = "Mango",
        size = "28", price = "60.000", isExchange = false,
        condition = "Buen estado", category = "Pantalones",
        imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?q=80&w=500&auto=format&fit=crop",
        sellerName = "Camila T.", sellerCity = "Medellín"
    ),
    MarketplaceItem(
        id = "3", title = "Vestido floral midi", brand = "H&M",
        size = "S", price = "75.000", isExchange = false,
        condition = "Como nuevo", category = "Vestidos",
        imageUrl = "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?q=80&w=500&auto=format&fit=crop",
        sellerName = "Sara M.", sellerCity = "Cali",
        isFavorited = true
    ),
    MarketplaceItem(
        id = "4", title = "Chaqueta de cuero", brand = "Stradivarius",
        size = "M", price = "120.000", isExchange = false,
        condition = "Nuevo", category = "Abrigos",
        imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?q=80&w=500&auto=format&fit=crop",
        sellerName = "Laura P.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "5", title = "Top de seda blanco", brand = "Pull&Bear",
        size = "XS", price = "55.000", isExchange = false,
        condition = "Nuevo", category = "Tops",
        imageUrl = "https://images.unsplash.com/photo-1515347648399-0f374c43e80d?q=80&w=500&auto=format&fit=crop",
        sellerName = "Isabela V.", sellerCity = "Barranquilla"
    ),
    MarketplaceItem(
        id = "6", title = "Falda plisada verde", brand = "Shein",
        size = "L", price = "35.000", isExchange = false,
        condition = "Buen estado", category = "Faldas",
        imageUrl = "https://images.unsplash.com/photo-1583337130417-3346a1be7dee?q=80&w=500&auto=format&fit=crop",
        sellerName = "Ana G.", sellerCity = "Medellín"
    ),
    MarketplaceItem(
        id = "7", title = "Cardigan oversize crema", brand = "Bershka",
        size = "M/L", price = "45.000", isExchange = false,
        condition = "Como nuevo", category = "Tops",
        imageUrl = "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?q=80&w=500&auto=format&fit=crop",
        sellerName = "Sofía N.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "8", title = "Pantalón cargo caqui", brand = "Zara",
        size = "38", price = "70.000", isExchange = false,
        condition = "Buen estado", category = "Pantalones",
        imageUrl = "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?q=80&w=500&auto=format&fit=crop",
        sellerName = "Daniela C.", sellerCity = "Cali"
    ),
)

val marketplaceCategories = listOf("Todo", "Tops", "Pantalones", "Vestidos", "Faldas", "Abrigos")

// ── Pantalla principal ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceHomeScreen(
    onProductClick: (String) -> Unit,
    onSellClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Todo") }
    var selectedFilter by remember { mutableStateOf("Todo") }
    var searchQuery by remember { mutableStateOf("") }
    var favoritedItems by remember { mutableStateOf(sampleMarketplaceItems.filter { it.isFavorited }.map { it.id }.toSet()) }

    val filtered = sampleMarketplaceItems.filter { item ->
        val matchesCategory = selectedCategory == "Todo" || item.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.brand.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSellClick,
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text("Publicar prenda") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Tienda",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Compra, vende e intercambia prendas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar prendas, marcas…") },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                        trailingIcon = {
                            AnimatedVisibility(visible = searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Outlined.Close, contentDescription = "Limpiar")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Todo", "Ofertas", "Nuevo").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            leadingIcon = if (isSelected) {{
                                Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }} else null
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(marketplaceCategories) { category ->
                        val isSelected = selectedCategory == category
                        AssistChip(
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = if (isSelected)
                                BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            else
                                AssistChipDefaults.assistChipBorder(true)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "${filtered.size} prendas encontradas",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            val chunked = filtered.chunked(2)
            items(chunked) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        val isFav = item.id in favoritedItems
                        MarketplaceItemCard(
                            item = item,
                            isFavorited = isFav,
                            onFavoriteToggle = {
                                favoritedItems = if (isFav)
                                    favoritedItems - item.id
                                else
                                    favoritedItems + item.id
                            },
                            onClick = { onProductClick(item.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }

            if (filtered.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "No encontramos prendas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Intenta con otros filtros o busca algo diferente",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ── Card individual de prenda (Mejorado con manejo de Carga y Error) ──────

@Composable
private fun MarketplaceItemCard(
    item: MarketplaceItem,
    isFavorited: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
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
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = item.condition,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (item.isExchange) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd),
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                Icons.Outlined.SwapHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "Swap",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorited) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorited) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.brand,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Talla ${item.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "$ ${item.price}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.sellerCity,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun categoryIcon(category: String): ImageVector = when (category) {
    "Tops" -> Icons.Outlined.Checkroom
    "Pantalones" -> Icons.Outlined.Straighten
    "Vestidos" -> Icons.Outlined.Face
    "Faldas" -> Icons.Outlined.Straighten
    "Abrigos" -> Icons.Outlined.AcUnit
    else -> Icons.Outlined.Checkroom
}
