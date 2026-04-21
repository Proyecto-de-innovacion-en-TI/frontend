package com.example.kloset.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    var name              by remember { mutableStateOf("") }
    var email             by remember { mutableStateOf("") }
    var password          by remember { mutableStateOf("") }
    var confirmPassword   by remember { mutableStateOf("") }
    var passwordVisible   by remember { mutableStateOf(false) }
    var confirmVisible    by remember { mutableStateOf(false) }
    var isLoading         by remember { mutableStateOf(false) }

    var nameError         by remember { mutableStateOf<String?>(null) }
    var emailError        by remember { mutableStateOf<String?>(null) }
    var passwordError     by remember { mutableStateOf<String?>(null) }
    var confirmError      by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()

    fun validate(): Boolean {
        nameError     = if (name.isBlank()) "Ingresa tu nombre" else null
        emailError    = if (email.isBlank() || !email.contains("@")) "Ingresa un correo válido" else null
        passwordError = when {
            password.length < 6                -> "Mínimo 6 caracteres"
            !password.any { it.isDigit() }     -> "Debe contener al menos un número"
            !password.any { it.isUpperCase() } -> "Debe contener al menos una mayúscula"
            else                               -> null
        }
        confirmError = if (confirmPassword != password) "Las contraseñas no coinciden" else null
        return listOf(nameError, emailError, passwordError, confirmError).all { it == null }
    }

    fun onRegisterClick() {
        if (!validate()) return
        isLoading = true
        // TODO: llamar al ViewModel/repositorio
        isLoading = false
        onRegistered()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // ── Header ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint               = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = "Tu armario inteligente te espera",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ── Nombre ────────────────────────────────────────────────
            OutlinedTextField(
                value         = name,
                onValueChange = { name = it; nameError = null },
                label         = { Text("Nombre completo") },
                leadingIcon   = { Icon(Icons.Outlined.Person, contentDescription = null) },
                isError       = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction    = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier   = Modifier.fillMaxWidth(),
                shape      = MaterialTheme.shapes.small
            )

            Spacer(Modifier.height(12.dp))

            // ── Email ─────────────────────────────────────────────────
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it; emailError = null },
                label         = { Text("Correo electrónico") },
                leadingIcon   = { Icon(Icons.Outlined.Email, contentDescription = null) },
                isError       = emailError != null,
                supportingText = emailError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier   = Modifier.fillMaxWidth(),
                shape      = MaterialTheme.shapes.small
            )

            Spacer(Modifier.height(12.dp))

            // ── Contraseña ────────────────────────────────────────────
            OutlinedTextField(
                value         = password,
                onValueChange = { password = it; passwordError = null },
                label         = { Text("Contraseña") },
                leadingIcon   = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible) Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                isError        = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier   = Modifier.fillMaxWidth(),
                shape      = MaterialTheme.shapes.small
            )

            // Indicador de fortaleza
            if (password.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                PasswordStrengthIndicator(password = password)
            }

            Spacer(Modifier.height(12.dp))

            // ── Confirmar contraseña ──────────────────────────────────
            OutlinedTextField(
                value         = confirmPassword,
                onValueChange = { confirmPassword = it; confirmError = null },
                label         = { Text("Confirmar contraseña") },
                leadingIcon   = { Icon(Icons.Outlined.LockOpen, contentDescription = null) },
                trailingIcon  = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            imageVector        = if (confirmVisible) Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility,
                            contentDescription = if (confirmVisible) "Ocultar" else "Mostrar"
                        )
                    }
                },
                visualTransformation = if (confirmVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                isError        = confirmError != null,
                supportingText = confirmError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus(); onRegisterClick() }
                ),
                singleLine = true,
                modifier   = Modifier.fillMaxWidth(),
                shape      = MaterialTheme.shapes.small
            )

            Spacer(Modifier.height(28.dp))

            // ── Botón principal ───────────────────────────────────────
            Button(
                onClick  = { onRegisterClick() },
                enabled  = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.small
            ) {
                AnimatedContent(
                    targetState = isLoading,
                    label       = "register_button"
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(22.dp),
                            color       = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text  = "Crear cuenta",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Divider ───────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text  = "  o  ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // ── Botón Google ──────────────────────────────────────────
            OutlinedButton(
                onClick  = { /* TODO: Google Sign-In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector        = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text  = "Continuar con Google",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Ir a login ────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "¿Ya tienes cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onBack) {
                    Text(
                        text  = "Inicia sesión",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Indicador de fortaleza de contraseña ──────────────────────────────────
@Composable
private fun PasswordStrengthIndicator(password: String) {
    val strength = when {
        password.length >= 8
                && password.any { it.isDigit() }
                && password.any { it.isUpperCase() }
                && password.any { !it.isLetterOrDigit() } -> 3  // Fuerte
        password.length >= 6
                && (password.any { it.isDigit() }
                || password.any { it.isUpperCase() })     -> 2  // Media
        else                                          -> 1  // Débil
    }

    val (label, color) = when (strength) {
        3    -> "Contraseña fuerte" to MaterialTheme.colorScheme.secondary
        2    -> "Contraseña media"  to MaterialTheme.colorScheme.tertiary
        else -> "Contraseña débil"  to MaterialTheme.colorScheme.error
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier             = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index < strength) color
                            else MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}