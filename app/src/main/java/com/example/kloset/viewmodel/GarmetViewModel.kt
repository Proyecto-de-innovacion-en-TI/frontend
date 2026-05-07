package com.example.kloset.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.kloset.data.Garment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class GarmentViewModel : ViewModel() {

    private val _garments = MutableStateFlow<List<Garment>>(emptyList())
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