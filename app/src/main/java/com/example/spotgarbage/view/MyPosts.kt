package com.example.spotgarbage.view


import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPosts(navController: NavController, complaintViewModel: ComplaintViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val complaintList = complaintViewModel.complaintList
    val uid= FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(Unit) {
        complaintViewModel.fetchPosts()
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
                    label = { Text(text = "Home", color = primary_light) },
                    icon = { Icon(painter = painterResource(id= R.drawable.home), contentDescription = "Delivery",tint = primary_light,modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("Home")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "My Posts", color = primary_light) },
                    icon = { Icon(painter = painterResource(id= R.drawable.mail), contentDescription = "Delivery",tint = primary_light,modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("MyPosts")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Add Complaint", color = primary_light) },
                    icon = { Icon(painter = painterResource(id= R.drawable.feedback), contentDescription = "Delivery", tint = primary_light,modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("addComplaint")
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Track Complaint", color = primary_light) },
                    icon = { Icon(painter = painterResource(id= R.drawable.delivery), contentDescription = "Delivery",tint = primary_light,modifier = Modifier.size(24.dp)) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate("TrackComplaint")
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
        })
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "My Complaints") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = primary_dark,
                        titleContentColor = White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = White)
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
                    .padding(start = 10.dp,end=10.dp)
            ) {


                if (complaintList.isEmpty()) {
                    Text("No complaints found", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(complaintList.size) { index ->
                            if (complaintList[index].userId == uid) {
                                MyComplaintItem(complaintList[index], onclick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "complaint",
                                        complaintList[index]
                                    )
                                    navController.navigate("MyDetailScreen")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyComplaintItem(complaint: Complaint,onclick:()->(Unit)) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Status: ", fontWeight = FontWeight.Bold)
                    Text(
                        text = complaint.status,
                        color = if (complaint.status == "Pending") Color.Red else Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }
}
