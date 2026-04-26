package com.rahulpahuja.waves.module.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.rahulpahuja.waves.R
import com.rahulpahuja.waves.ui.navigation.Screen
import com.rahulpahuja.waves.ui.theme.WavesTheme
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginClick: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                Log.d("LoginScreen", "Google Sign-In Success, token found")
                viewModel.signInWithGoogle(token)
            } ?: run {
                Log.e("LoginScreen", "Google Sign-In failed: ID Token is null")
                Toast.makeText(context, "Google Sign-In failed: ID Token is null", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google Sign-In error code: ${e.statusCode}", e)
            Toast.makeText(context, "Google Error: ${e.message} (Code: ${e.statusCode})", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(authState) {
        Log.d("LoginScreen", "Current AuthState: $authState")
        when (authState) {
            is AuthState.Success -> {
                Log.d("LoginScreen", "Navigating to Home/AdminDashboard")
                onLoginClick((authState as AuthState.Success).isAdmin)
            }
            is AuthState.NeedsRoleSelection -> {
                Log.d("LoginScreen", "Navigating to RoleSelection")
                navController.navigate(Screen.RoleSelection.route)
            }
            is AuthState.PendingApproval -> {
                Log.d("LoginScreen", "Navigating to WaitingApproval")
                navController.navigate(Screen.WaitingApproval.route)
            }
            is AuthState.Error -> {
                Log.e("LoginScreen", "Auth Error: ${(authState as AuthState.Error).message}")
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    LoginScreenContent(
        email = email,
        password = password,
        passwordVisible = passwordVisible,
        isLoading = authState is AuthState.Loading,
        onLoginClick = { 
            viewModel.onLoginClick()
        },
        onGoogleLoginClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            
            // Sign out of Google to force account picker
            googleSignInClient.signOut().addOnCompleteListener {
                launcher.launch(googleSignInClient.signInIntent)
            }
        },
        onForgotPasswordClick = onForgotPasswordClick,
        onSignUpClick = onSignUpClick,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onPasswordVisibilityToggle = { viewModel.onPasswordVisibilityToggle() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    passwordVisible: Boolean,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Image Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.2f)), // Placeholder
                contentAlignment = Alignment.Center
            ) {
                 // Image(painter = painterResource(id = R.drawable.logo_placeholder), contentDescription = null)
                Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Turn Up the Volume.",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Manage your classes and connect with the community.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            // Email Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Email Address", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = { Text("producer@example.com or admin@example.com", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E232F),
                        unfocusedContainerColor = Color(0xFF1E232F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Password", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text("........", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.Gray) },
                    trailingIcon = {
                        Row {
                            if (password.isNotEmpty()) {
                                IconButton(onClick = { onPasswordChange("") }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear password", tint = Color.Gray)
                                }
                            }
                            IconButton(onClick = onPasswordVisibilityToggle) {
                                Icon(
                                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E232F),
                        unfocusedContainerColor = Color(0xFF1E232F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
            
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = onForgotPasswordClick) {
                    Text("Forgot Password?", color = Color(0xFF2962FF))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onLoginClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Log In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
                Text("Or", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                // Google Icon placeholder
                Text("G", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp) 
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Color.Gray, fontSize = 14.sp)
                TextButton(onClick = onSignUpClick) {
                    Text("Sign Up", color = Color(0xFF2962FF), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WavesTheme {
        LoginScreenContent(
            email = "",
            password = "",
            passwordVisible = false,
            isLoading = false,
            onLoginClick = {},
            onGoogleLoginClick = {},
            onForgotPasswordClick = {},
            onSignUpClick = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {}
        )
    }
}
