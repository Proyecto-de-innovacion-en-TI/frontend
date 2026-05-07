package com.example.kloset.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.kloset.data.Garment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class GarmentViewModel : ViewModel() {

    private val _garments = MutableStateFlow<List<Garment>>(listOf(

        Garment(
            id = UUID.randomUUID().toString(),
            name = "Camiseta Blanca",
            category = "Tops",
            imageUri = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?q=80&w=400"
        ),
        Garment(
            id = UUID.randomUUID().toString(),
            name = "Vaqueros Azules",
            category = "Pantalones",
            imageUri = "https://images.unsplash.com/photo-1542272604-787c3835535d?q=80&w=400"
        ),
        Garment(
            id = UUID.randomUUID().toString(),
            name = "Vestido de Verano",
            category = "Vestidos",
            imageUri = "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?q=80&w=400"
        ),
        Garment(
            id = UUID.randomUUID().toString(),
            name = "Zapatillas Urbanas",
            category = "Calzado",
            imageUri = "https://images.unsplash.com/photo-1549298916-b41d501d3772?q=80&w=400"
        )
    ))
    val garments: StateFlow<List<Garment>> = _garments.asStateFlow()

    fun addGarment(name: String, category: String, imageUri: Uri?) {
        val new = Garment(
            id = UUID.randomUUID().toString(),
            name = name,
            category = category,
            imageUri = imageUri?.toString()
        )
        _garments.value = _garments.value + new
    }
}