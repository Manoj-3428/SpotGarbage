package com.example.spotgarbage.view

import android.R
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.ui.theme.primary_dark
import com.example.spotgarbage.ui.theme.primary_light
import com.example.spotgarbage.ui.theme.secondary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(complaint: Complaint?,navController: NavController) {
    val scrollState = rememberScrollState()
    val uid= FirebaseAuth.getInstance().currentUser?.uid
    val coroutineScope= rememberCoroutineScope()
    val context= LocalContext.current
    complaint?.let {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 20.dp)
                ) {
                    AsyncImage(
                        model = complaint.imageUri,
                        contentDescription = "Complaint Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().size(200.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Text(
                        text = "Posted by :",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = complaint.username.uppercase(),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),

                    ) {
                    Text(text = "Posted on :", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(7.dp))

                    Text(text = "${complaint.dayOfWeek}, ${complaint.formattedDate} at ${complaint.formattedTime}")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Text(text = "Location :", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(text = "${complaint.location}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Text(text = "More info :", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "${complaint.description}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Text(text = "For queries :", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(text = "${complaint.email}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Text(text = "Status :", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "${complaint.status }", color = if(complaint.status=="Pending") Color.Red else Color.Green)
                }
                if(complaint.status=="Done"){
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                    ) {
                        Text(text = "Collected on:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(1.dp))
                        Text(text = "${complaint.clearedOn}")
                    }
                }
            }
            if(uid==complaint.userId){
                val isDelete= remember { mutableStateOf(false) }
                var showDialog = remember { mutableStateOf(false) }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                    enabled = !isDelete.value,
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = secondary),
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    if (isDelete.value) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Deleting...")
                    } else {
                        Text(text = "Delete this complaint")
                    }
                }

                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp),
                        shape = RoundedCornerShape(10.dp),
                        title = { Text("Delete Complaint?") },
                        text = { Text("Are you sure you want to delete this complaint? This action cannot be undone.") },
                        confirmButton = {
                            TextButton(colors = ButtonDefaults.buttonColors(contentColor = primary_dark), onClick = {
                                showDialog.value = false
                                isDelete.value = true
                                coroutineScope.launch {
                                    DeleteMyComplaint(navController, complaint.postId.toString(), onSuccess = {
                                        DeleteMyImage(complaint.imageUri, onSuccess = {
                                            Toast.makeText(context, "Post Deleted successfully", Toast.LENGTH_SHORT).show()
                                            isDelete.value = false
                                            navController.navigate("Home") {
                                                popUpTo(0)
                                            }
                                        }, onFailure = {
                                            isDelete.value = false
                                        })
                                    }, onFailure = {
                                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                        isDelete.value = false
                                    })
                                }
                            }) {
                                Text("Yes, Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog.value = false },colors = ButtonDefaults.buttonColors(contentColor = primary_dark),) {
                                Text("Cancel")
                            }
                        }
                    )
                }

            }
        }
    }
}

