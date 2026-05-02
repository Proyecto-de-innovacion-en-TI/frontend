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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Modelos de datos ──────────────────────────────────────────────────────

data class MarketplaceItem(
    val id: String,
    val title: String,
    val brand: String,
    val size: String,
    val price: String,      // null = intercambio
    val isExchange: Boolean,
    val condition: String,  // "Nuevo", "Como nuevo", "Buen estado", "Usado"
    val category: String,
    val colorHex: Long,     // color representativo de la prenda
    val sellerName: String,
    val sellerCity: String,
    val isFavorited: Boolean = false
)

val sampleMarketplaceItems = listOf(
    MarketplaceItem(
        id = "1", title = "Blazer lino beige", brand = "Zara",
        size = "M", price = "85.000", isExchange = false,
        condition = "Como nuevo", category = "Tops",
        colorHex = 0xFFD4C5A9L, sellerName = "Valentina R.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "2", title = "Jeans wide leg", brand = "Mango",
        size = "28", price = "60.000", isExchange = false,
        condition = "Buen estado", category = "Pantalones",
        colorHex = 0xFF3A5A8AL, sellerName = "Camila T.", sellerCity = "Medellín"
    ),
    MarketplaceItem(
        id = "3", title = "Vestido floral midi", brand = "H&M",
        size = "S", price = "75.000", isExchange = false,
        condition = "Como nuevo", category = "Vestidos",
        colorHex = 0xFFE8A0A0L, sellerName = "Sara M.", sellerCity = "Cali",
        isFavorited = true
    ),
    MarketplaceItem(
        id = "4", title = "Chaqueta de cuero", brand = "Stradivarius",
        size = "M", price = "120.000", isExchange = false,
        condition = "Nuevo", category = "Abrigos",
        colorHex = 0xFF2A1A0AL, sellerName = "Laura P.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "5", title = "Top de seda blanco", brand = "Pull&Bear",
        size = "XS", price = "55.000", isExchange = false,
        condition = "Nuevo", category = "Tops",
        colorHex = 0xFFF5F0EAL, sellerName = "Isabela V.", sellerCity = "Barranquilla"
    ),
    MarketplaceItem(
        id = "6", title = "Falda plisada verde", brand = "Shein",
        size = "L", price = "35.000", isExchange = false,
        condition = "Buen estado", category = "Faldas",
        colorHex = 0xFF5A8A5AL, sellerName = "Ana G.", sellerCity = "Medellín"
    ),
    MarketplaceItem(
        id = "7", title = "Cardigan oversize crema", brand = "Bershka",
        size = "M/L", price = "45.000", isExchange = false,
        condition = "Como nuevo", category = "Tops",
        colorHex = 0xFFE8DFD0L, sellerName = "Sofía N.", sellerCity = "Bogotá"
    ),
    MarketplaceItem(
        id = "8", title = "Pantalón cargo caqui", brand = "Zara",
        size = "38", price = "70.000", isExchange = false,
        condition = "Buen estado", category = "Pantalones",
        colorHex = 0xFF8A7A5AL, sellerName = "Daniela C.", sellerCity = "Cali"
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
    var selectedFilter by remember { mutableStateOf("Todo") } // "Todo", "Venta"
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
            // ── Header ────────────────────────────────────────────────
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

                    // Barra de búsqueda
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

            // ── Filtros ──────────────────────────────────────────────
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

            // ── Categorías ────────────────────────────────────────────
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

            // ── Contador de resultados ────────────────────────────────
            item {
                Text(
                    text = "${filtered.size} prendas encontradas",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Grid de prendas (2 columnas simuladas con Row) ────────
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
                    // Rellena si la fila tiene un solo item
                    if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }

            // Estado vacío
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

// ── Card individual de prenda ─────────────────────────────────────────────

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
            // Imagen simulada con color de prenda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(item.colorHex).copy(alpha = 0.9f),
                                Color(item.colorHex).copy(alpha = 0.6f)
                            )
                        )
                    )
            ) {
                // Ícono de categoría centrado
                Icon(
                    imageVector = categoryIcon(item.category),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )

                // Badge condición
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

                // Badge intercambio
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

                // Botón favorito
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

            // Info de la prenda
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

// Icono según categoría
fun categoryIcon(category: String): ImageVector = when (category) {
    "Tops" -> Icons.Outlined.Checkroom
    "Pantalones" -> Icons.Outlined.Straighten
    "Vestidos" -> Icons.Outlined.Face
    "Faldas" -> Icons.Outlined.Straighten
    "Abrigos" -> Icons.Outlined.AcUnit
    else -> Icons.Outlined.Checkroom
}