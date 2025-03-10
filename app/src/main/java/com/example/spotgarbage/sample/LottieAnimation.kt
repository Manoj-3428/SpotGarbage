package com.example.spotgarbage.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun LottieAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.example.spotgarbage.R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        speed = 1f,
        iterations = LottieConstants.IterateForever
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(composition = composition, progress = { progress })
    }
}
