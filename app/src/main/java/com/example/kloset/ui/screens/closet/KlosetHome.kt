package com.example.kloset.ui.screens.closet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast

// Modelo de datos para las prendas
data class Garment(
    val id: String,
    val name: String,
    val category: String,
    val colorHex: Long
)

// Datos de ejemplo
val sampleGarments = listOf(
    Garment("1", "Blusa Seda", "Tops", 0xFFF5F0EA),
    Garment("2", "Jeans Mom", "Pantalones", 0xFF3A5A8A),
    Garment("3", "Vestido Noche", "Vestidos", 0xFF2A1A0A),
    Garment("4", "Chaqueta Cuero", "Abrigos", 0xFF1A1A1A),
    Garment("5", "Falda Midi", "Faldas", 0xFF5A8A5A),
    Garment("6", "Top Crop", "Tops", 0xFFE8A0A0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlosetHome(
    onAddGarment: () -> Unit,
    onGarmentClick: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Armario", fontWeight = FontWeight.Bold) },
                actions = {
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddGarment()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Prenda")
            }
        }
    ) { padding ->
        // Cuadrícula de items
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleGarments) { garment ->
                GarmentItemCard(
                    garment = garment,
                    onClick = { onGarmentClick(garment.id) }
                )
            }
        }
    }
}

@Composable
fun GarmentItemCard(garment: Garment, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            // Representación visual de la prenda (color por ahora)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(garment.colorHex))
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = garment.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = garment.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
