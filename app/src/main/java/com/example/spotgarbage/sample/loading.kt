package com.example.spotgarbage.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SimpleLoadingBar() {
    var isLoading = remember { mutableStateOf(false) }

    Column {
        if (isLoading.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Button(onClick = { isLoading.value = true }, modifier = Modifier.padding(16.dp)) {
            Text(text="Start Loading")
        }
    }
}