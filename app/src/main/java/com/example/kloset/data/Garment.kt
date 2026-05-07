package com.example.kloset.data

data class Garment(
    val id: String,
    val name: String,
    val category: String,
    val colorHex: Long = 0xFFE0E0E0,
    val imageUri: String? = null   // ← URI de la foto tomada/seleccionada
)