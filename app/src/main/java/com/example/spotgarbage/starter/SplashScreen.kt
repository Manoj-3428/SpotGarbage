package com.example.spotgarbage.starter
import com.example.spotgarbage.R

import androidx.compose.foundation.Image
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
@Composable
fun splash1(navController: NavController){
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("splash2")
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Garbage Detector", fontSize = 28.sp, fontWeight = FontWeight.Bold,
            color=colorResource(id= R.color.primary), modifier = Modifier.padding(top=40.dp))
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Image(painter = painterResource(R.drawable.app_icon), contentDescription = "App icon",
            modifier = Modifier.height(250.dp).width(280.dp).padding(top=8.dp, bottom = 10.dp))
        Text(text="Spot the garbage", color = colorResource(R.color.primary_light))

    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text="Powered by Manoj", color = colorResource(R.color.secondary_light))

    }
}