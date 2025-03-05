package com.example.spotgarbage.starter

import com.example.spotgarbage.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun splash2(navController: NavController){

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.app_icon),
            contentDescription = "App Icon",
            modifier = Modifier.height(350.dp).width(350.dp).padding(start=30.dp, end = 30.dp, bottom = 10.dp))
        Text(text="Spot the Garbage", color = colorResource(R.color.primary_light))
        Text(text="Let's be responsible", modifier = Modifier.padding(top=20.dp))
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
        Button(onClick = {
            navController.navigate("login")
        }, modifier = Modifier.fillMaxWidth().height(50.dp).padding(start = 15.dp, end = 15.dp), shape = RoundedCornerShape(8.dp),colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary))
        )
        {
            Text(text = "Continue")
        }
        Text(text="Powered by Manoj",color= colorResource(R.color.secondary_light), modifier = Modifier.padding(bottom = 20.dp, top = 20.dp))
    }



}