package com.example.kloset.ui.screens.closet

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import com.example.kloset.viewmodel.GarmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGarmentScreen(
    viewModel: GarmentViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // URI temporal para la foto de cámara
    val cameraImageUri = remember {
        createTempImageUri(context)
    }

    // ─── Dialogo para elegir fuente de imagen ───────────────────────────────
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // ─── Dialogo de permisos denegados ──────────────────────────────────────
    var showPermissionRationale by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // ─── Launcher: tomar foto con cámara ────────────────────────────────────
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) selectedImageUri = cameraImageUri
    }

    // ─── Launcher: seleccionar de galería ───────────────────────────────────
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    // ─── Launcher: solicitar permiso de cámara ───────────────────────────────
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(cameraImageUri)
        } else {
            showPermissionRationale = true
            pendingAction = { cameraLauncher.launch(cameraImageUri) }
        }
    }

    // ─── Launcher: solicitar permiso de galería ──────────────────────────────
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            showPermissionRationale = true
            pendingAction = { galleryLauncher.launch("image/*") }
        }
    }

    // ─── Funciones helper de permiso ────────────────────────────────────────
    fun launchCamera() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                cameraLauncher.launch(cameraImageUri)
            }
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun launchGallery() {
        val readPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        when {
            ContextCompat.checkSelfPermission(context, readPermission)
                    == PackageManager.PERMISSION_GRANTED -> {
                galleryLauncher.launch("image/*")
            }
            else -> galleryPermissionLauncher.launch(readPermission)
        }
    }

    // ─── Dialogo: elegir cámara o galería ───────────────────────────────────
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Añadir foto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                showImageSourceDialog = false
                                launchCamera()
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Text("Tomar foto")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                showImageSourceDialog = false
                                launchGallery()
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Text("Elegir de galería")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ─── Dialogo: permiso denegado ───────────────────────────────────────────
    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Permiso requerido") },
            text = {
                Text(
                    "Para agregar fotos a tu armario necesitamos acceso a tu cámara o galería. " +
                            "Por favor actívalo desde Ajustes > Aplicaciones > Kloset > Permisos."
                )
            },
            confirmButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    // ─── UI Principal ────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Prenda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Selector de foto ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Foto de la prenda",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Toca para añadir foto",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Botón cambiar foto (visible solo si ya hay imagen)
            if (selectedImageUri != null) {
                OutlinedButton(
                    onClick = { showImageSourceDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar foto")
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la prenda") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (ej: Tops, Pantalones)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.addGarment(name, category, selectedImageUri)
                    onSaved()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && category.isNotBlank()
            ) {
                Text("Guardar en mi Armario")
            }
        }
    }
}

// ─── Crea un URI temporal para guardar la foto de cámara ─────────────────────
fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "garment_${System.currentTimeMillis()}",
        ".jpg",
        context.cacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}
