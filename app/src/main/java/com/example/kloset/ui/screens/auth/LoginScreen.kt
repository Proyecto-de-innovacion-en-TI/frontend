// ui/screens/auth/LoginScreen.kt
package com.example.kloset.ui.screens.auth

import androidx.compose.animation.*
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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading       by remember { mutableStateOf(false) }
    var emailError      by remember { mutableStateOf<String?>(null) }
    var passwordError   by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()

    fun validate(): Boolean {
        emailError    = if (email.isBlank() || !email.contains("@")) "Ingresa un correo válido" else null
        passwordError = if (password.length < 6) "Mínimo 6 caracteres" else null
        return emailError == null && passwordError == null
    }

    fun onLoginClick() {
        if (!validate()) return
        isLoading = true
        // TODO: llamar al ViewModel/repositorio
        // Simulación temporal:
        isLoading = false
        onLoginSuccess()
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
            Spacer(Modifier.height(72.dp))

            // ── Header ───────────────────────────────────────────────
            Text(
                text  = "Kloset",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text  = "Repetir outfit no es lo mismo\nque repetir estilo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(48.dp))

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
                            imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff
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
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus(); onLoginClick() }
                ),
                singleLine = true,
                modifier   = Modifier.fillMaxWidth(),
                shape      = MaterialTheme.shapes.small
            )

            // ── Olvidé mi contraseña ──────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick  = { /* TODO: ForgotPasswordScreen */ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text  = "¿Olvidaste tu contraseña?",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Botón principal ───────────────────────────────────────
            Button(
                onClick  = { onLoginClick() },
                enabled  = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.small
            ) {
                AnimatedContent(
                    targetState = isLoading,
                    label       = "login_button"
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color    = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text  = "Ingresar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Divider ───────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text     = "  o  ",
                    style    = MaterialTheme.typography.bodySmall,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
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

            // ── Ir a registro ─────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "¿No tienes cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onGoToRegister) {
                    Text(
                        text  = "Regístrate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}