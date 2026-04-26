package com.rahulpahuja.waves.module.auth.forgotpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import com.rahulpahuja.waves.ui.theme.WavesTheme

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
) {
    ForgotPasswordContent()
}

@Composable
fun ForgotPasswordContent() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Forgot Password Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    WavesTheme {
        ForgotPasswordContent()
    }
}
