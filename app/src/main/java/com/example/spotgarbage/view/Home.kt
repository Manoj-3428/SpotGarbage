package com.example.spotgarbage.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotgarbage.R
import com.example.spotgarbage.authentication.logout
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.ui.theme.primary_dark
import com.example.spotgarbage.ui.theme.primary_light
import com.example.spotgarbage.viewmodel.ComplaintViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, complaintViewModel: ComplaintViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4000) 
        isLoading = false
    }

    val complaintList = complaintViewModel.complaintList
    val filteredComplaints by remember {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                complaintList
            } else {
                complaintList.filter { it.username.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(250.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(primary_dark),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Profile image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
                NavigationDrawerItem(
                    label = { Text(text = "Profile", color = primary_light) },
                    icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile", tint = primary_light) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("profile")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "My Posts", color = primary_light) },
                    icon = { Icon(painter = painterResource(id = R.drawable.mail), contentDescription = "My Posts", tint = primary_light, modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("MyPosts")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Add Complaint", color = primary_light) },
                    icon = { Icon(painter = painterResource(id = R.drawable.feedback), contentDescription = "Add Complaint", tint = primary_light, modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("addComplaint")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Track Complaint", color = primary_light) },
                    icon = { Icon(painter = painterResource(id = R.drawable.delivery), contentDescription = "Track Complaint", tint = primary_light, modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        Toast.makeText(context, "Track complaint", Toast.LENGTH_SHORT).show()
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Logout", color = primary_light) },
                    icon = { Icon(painter = painterResource(id = R.drawable.logout), contentDescription = "Logout", tint = primary_light, modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        logout(onSuccess = {
                            Toast.makeText(context, "Successfully logged out from your account", Toast.LENGTH_LONG).show()
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        }, onFailure = {
                            Toast.makeText(context, "Failed to Login +$it", Toast.LENGTH_LONG).show()
                        })
                    }
                )
            }
        }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Garbage Detector") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = primary_dark,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(10.dp)
            ) {
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Complaints") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { /* Handle search action */ }
                    )
                )

                if (isLoading) {
                    // Show progress indicator when data is loading
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement =Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = primary_dark, modifier = Modifier.padding(bottom = 20.dp))
                        Text("Complaints are fetching please wait")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredComplaints.size) { index ->
                            ComplaintItem(filteredComplaints[index]) {
                                navController.currentBackStackEntry?.savedStateHandle?.set("complaint", filteredComplaints[index])
                                navController.navigate("DetailScreen")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComplaintItem(complaint: Complaint, onclick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .wrapContentSize()
            .clickable { onclick() },
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = primary_light),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = complaint.imageUri,
                contentDescription = "Complaint Image",
                placeholder = painterResource(R.drawable.post),
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = "Posted by: ${complaint.username}", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                Text(text = "${complaint.dayOfWeek}, ${complaint.formattedDate}, at ${complaint.formattedTime}", fontSize = 12.sp, modifier = Modifier.padding(top = 5.dp))
            }
        }
    }
}
