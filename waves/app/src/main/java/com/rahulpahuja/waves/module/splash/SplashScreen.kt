package com.rahulpahuja.waves.module.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rahulpahuja.waves.R
import com.rahulpahuja.waves.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disco")
    val destination by viewModel.destination.collectAsState()

    // Laser Rotation Animation
    val laserRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "laser"
    )

    // Background Color Animation
    val bgColorShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgShift"
    )

    // Beat Pulse Animation
    val beatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beat"
    )

    // Sound Wave Rings Animation
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rings"
    )
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringsScale"
    )

    LaunchedEffect(Unit) {
        viewModel.checkAuthState()
        delay(4000) // Vibe check time
        destination?.let { route ->
            navController.navigate(route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // Auto-navigate if state finishes after delay
    LaunchedEffect(destination) {
        if (destination != null) {
            delay(500) // Ensure a minimum experience
            navController.navigate(destination!!) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0C29),
                        Color(0xFF302B63),
                        Color(0xFF24243E)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Moving Laser Beams
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasCenter = center
            // Cyan Laser
            withTransform({
                rotate(laserRotation, canvasCenter)
            }) {
                drawRect(
                    color = Color(0xFF00E5FF),
                    topLeft = androidx.compose.ui.geometry.Offset(canvasCenter.x - 2.dp.toPx(), 0f),
                    size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height),
                    alpha = 0.15f
                )
            }
            // Pink Laser
            withTransform({
                rotate(laserRotation + 90f, canvasCenter)
            }) {
                drawRect(
                    color = Color(0xFFE91E63),
                    topLeft = androidx.compose.ui.geometry.Offset(canvasCenter.x - 2.dp.toPx(), 0f),
                    size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height),
                    alpha = 0.15f
                )
            }
        }

        // Neon Glow Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFE91E63), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(bgColorShift % 1000f, 500f),
                        radius = 800f
                    )
                )
        )

        // Sparkles (Disco ball effect)
        repeat(20) {
            Sparkle(infiniteTransition)
        }

        // Floating Musical Notes
        repeat(12) {
            FloatingNote(infiniteTransition)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Pulse Rings
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(150.dp).scale(ringScale)) {
                    drawCircle(
                        color = Color(0xFF2962FF),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 2.dp.toPx()),
                        alpha = ringAlpha
                    )
                }
                Canvas(modifier = Modifier.size(150.dp).scale(ringScale * 0.7f)) {
                    drawCircle(
                        color = Color(0xFFE91E63),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 1.5.dp.toPx()),
                        alpha = ringAlpha * 0.8f
                    )
                }

                // Logo with beat scale
                Image(
                    painter = painterResource(id = R.drawable.logo_placeholder),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(beatScale)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Neon Text
            Text(
                text = "WAVES ACADEMY",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp
            )
            Text(
                text = "THE BEAT STARTS HERE",
                color = Color(0xFF00E5FF), // Cyan Neon
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(0.8f)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Themed Progress
            LinearProgressIndicator(
                modifier = Modifier
                    .width(240.dp)
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color(0xFFE91E63),
                trackColor = Color.White.copy(alpha = 0.1f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "SYNCING BEATS...",
                color = Color.Gray,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Version Info
        Text(
            text = "v2.5.0 (PLATINUM EDITION)",
            color = Color.White.copy(alpha = 0.3f),
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun FloatingNote(transition: InfiniteTransition) {
    val randomX = remember { Random.nextFloat() }
    val randomY = remember { Random.nextFloat() }
    val randomDuration = remember { (3000..6000).random() }
    val randomIcon = remember { if (Random.nextBoolean()) Icons.Default.MusicNote else Icons.Default.GraphicEq }
    
    val driftY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -150f,
        animationSpec = infiniteRepeatable(
            animation = tween(randomDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )
    
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(randomDuration / 2, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val wobble by transition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = SineWaveEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = (randomX * 300).dp,
                top = (randomY * 600).dp
            )
    ) {
        Icon(
            imageVector = randomIcon,
            contentDescription = null,
            tint = if (Random.nextBoolean()) Color(0xFF2962FF) else Color(0xFFE91E63),
            modifier = Modifier
                .size(24.dp)
                .offset(y = driftY.dp)
                .rotate(wobble)
                .alpha(alpha)
        )
    }
}

@Composable
fun Sparkle(transition: InfiniteTransition) {
    val randomX = remember { Random.nextFloat() }
    val randomY = remember { Random.nextFloat() }
    val delay = remember { (0..2000).random() }
    
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1500
                0f at delay
                0.8f at delay + 200
                0f at delay + 400
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = (randomX * 400).dp, top = (randomY * 800).dp)
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .alpha(alpha)
                .background(Color.White, CircleShape)
        )
    }
}

val SineWaveEasing = Easing { fraction ->
    kotlin.math.sin(fraction * kotlin.math.PI).toFloat()
}
