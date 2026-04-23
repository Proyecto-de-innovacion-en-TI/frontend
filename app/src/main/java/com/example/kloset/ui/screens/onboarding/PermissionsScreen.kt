package com.example.kloset.ui.screens.onboarding

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

data class PermissionItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val description: String,
    val required: Boolean,
    val androidPermission: String
)

val permissionItems: List<PermissionItem>
    get() {
        val list = mutableListOf(
            PermissionItem(
                id                = "camera",
                icon              = Icons.Outlined.CameraAlt,
                title             = "Cámara",
                description       = "Para fotografiar tus prendas y digitalizarlas automáticamente en tu armario.",
                required          = true,
                androidPermission = Manifest.permission.CAMERA
            ),
            PermissionItem(
                id                = "gallery",
                icon              = Icons.Outlined.Photo,
                title             = "Galería",
                description       = "Para subir fotos existentes de tu ropa sin necesidad de tomarlas en el momento.",
                required          = true,
                androidPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(
                PermissionItem(
                    id                = "notifications",
                    icon              = Icons.Outlined.Notifications,
                    title             = "Notificaciones",
                    description       = "Para avisarte sobre nuevos outfits sugeridos y actividad en el marketplace.",
                    required          = false,
                    androidPermission = Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
        return list
    }

@Composable
fun PermissionsScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    val grantedPermissions = remember { mutableStateMapOf<String, Boolean>() }
    val scrollState = rememberScrollState()

    // Inicializar estado de permisos al entrar
    LaunchedEffect(Unit) {
        permissionItems.forEach { item ->
            grantedPermissions[item.id] = context.checkPermission(item.androidPermission)
        }
    }

    // Launchers para cada permiso
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        grantedPermissions["camera"] = isGranted
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        grantedPermissions["gallery"] = isGranted
    }

    val notificationsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        grantedPermissions["notifications"] = isGranted
    }

    val items = permissionItems
    val requiredGranted = items
        .filter { it.required }
        .all { grantedPermissions[it.id] == true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // ── Header ────────────────────────────────────────────────
            Icon(
                imageVector        = Icons.Outlined.Security,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text      = "Permisos necesarios",
                style     = MaterialTheme.typography.headlineMedium,
                color     = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Kloset necesita algunos permisos para\nfuncionar correctamente",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // ── Lista de permisos ─────────────────────────────────────
            items.forEach { permission ->
                val isGranted = grantedPermissions[permission.id] == true

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (isGranted)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícono del permiso
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = if (isGranted)
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.small
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = if (isGranted) Icons.Outlined.Check else permission.icon,
                                contentDescription = null,
                                tint               = if (isGranted)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier           = Modifier.size(24.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text  = permission.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (permission.required) {
                                    Spacer(Modifier.width(6.dp))
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text  = "Requerido",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text  = permission.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        // Botón conceder / concedido
                        AnimatedVisibility(visible = !isGranted) {
                            FilledTonalButton(
                                onClick = {
                                    when (permission.id) {
                                        "camera" -> cameraLauncher.launch(permission.androidPermission)
                                        "gallery" -> galleryLauncher.launch(permission.androidPermission)
                                        "notifications" -> notificationsLauncher.launch(permission.androidPermission)
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text  = "Permitir",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))

            // ── Nota de privacidad ────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier          = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.Info,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = "Tus datos se almacenan de forma cifrada bajo la Ley 1581 de 2012. Nunca compartimos tu información sin tu consentimiento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Botón continuar ───────────────────────────────────────
            Button(
                onClick  = onFinish,
                enabled  = requiredGranted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text  = if (requiredGranted) "¡Empecemos!" else "Acepta los permisos requeridos",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

private fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
