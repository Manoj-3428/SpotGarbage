package com.example.spotgarbage.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotgarbage.R
import com.example.spotgarbage.dataclasses.Profiles
import com.example.spotgarbage.firebase.profiles
import com.example.spotgarbage.ui.theme.primary_dark
import com.example.spotgarbage.ui.theme.primary_light
import com.example.spotgarbage.ui.theme.secondary_light
import com.example.spotgarbage.ui.theme.white
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profile(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val role = remember { mutableStateOf("") }
    val imageuri = remember { mutableStateOf<Uri?>(null) }
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val auth = FirebaseAuth.getInstance()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading=remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading.value=false
    }
    val singleImagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageuri.value = uri
        }
    }

    val userId = auth.currentUser?.uid
    if (userId != null) {
        LaunchedEffect(userId) {
            loadUserData(db, userId, role, name, email, phone, address, imageuri)
            Log.d("User uid :",userId)
        }
    }
    if (isLoading.value == true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = primary_dark)
        }
    }
    else {
    Column(modifier = Modifier.fillMaxSize().background(white).verticalScroll(scrollState).imePadding(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().height(250.dp).padding(), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = if (!imageuri.value.toString().isNullOrEmpty()) imageuri.value else R.drawable.avatar,
                contentDescription = "Profile photo",
                modifier = Modifier.clip(CircleShape).border(4.dp, primary_dark, CircleShape).
                                    clip(CircleShape).size(150.dp), contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { singleImagePicker.launch("image/*") },
                modifier = Modifier.padding(top = 120.dp, start = 105.dp).clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(contentColor = white, containerColor = primary_light)
            ) {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp),
                    tint = primary_dark
                )
            }
        }
        OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light),
                        unfocusedBorderColor = colorResource(R.color.black),
                        focusedLabelColor = colorResource(R.color.secondary_light),
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = "Email", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.mail),
                            contentDescription = "Email",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light),
                        unfocusedBorderColor = colorResource(R.color.black),
                        focusedLabelColor = colorResource(R.color.secondary_light),
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )
                )
                OutlinedTextField(
                    value = phone.value,
                    onValueChange = { phone.value = it },
                    label = { Text(text = "Phone number", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light),
                        unfocusedBorderColor = colorResource(R.color.black),
                        focusedLabelColor = colorResource(R.color.secondary_light),
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )
                )
                OutlinedTextField(
                    value = address.value,
                    onValueChange = { address.value = it },
                    label = { Text(text = "Current address", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light),
                        unfocusedBorderColor = colorResource(R.color.black),
                        focusedLabelColor = colorResource(R.color.secondary_light),
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )
                )
            Spacer(modifier = Modifier.weight(1f))
            val isUploading = remember { mutableStateOf(false) }
            Button(
                onClick = {
                    isUploading.value = true
                    coroutineScope.launch {
                        profiles(
                            role.value,
                            name.value,
                            email.value,
                            phone.value,
                            imageuri.value,
                            address.value,
                            db,
                            storage,
                            auth,
                            context
                        ) {
                            isUploading.value = false
                        }
                    }
                },
                modifier = Modifier.wrapContentSize().padding(bottom=50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondary_light),
                shape = RoundedCornerShape(4.dp),
                enabled = !isUploading.value
            ) {
                if (isUploading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp), color = primary_dark)
                } else {
                    Text(text = "Save the data")
                }
            }

        }
        }
    }
suspend fun loadUserData(
    db: FirebaseFirestore,
    userId: String,
    role: MutableState<String>,
    name: MutableState<String>,
    email: MutableState<String>,
    phone: MutableState<String>,
    address: MutableState<String>,
    imageUri: MutableState<Uri?>
) {
    db.collection("users").document(userId).get().addOnSuccessListener { document ->
        val user = document.toObject(Profiles::class.java)
        if (user != null) {
            role.value=user.role
            name.value = user.name
            email.value = user.email
            phone.value = user.phone
            address.value = user.address
            imageUri.value = Uri.parse(user.uri)
        }
    }
}
