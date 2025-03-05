package com.example.spotgarbage.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.ui.theme.secondary
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyDetailScreen(complaint: Complaint?,navController: NavController) {
    val scrollState = rememberScrollState()
    val context=LocalContext.current
    complaint?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 70.dp)
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
            }
    val isDelete= remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = secondary),
                onClick = {
                    isDelete.value = true
                    coroutineScope.launch {
                        DeleteMyComplaint(complaint.postId.toString(), onSuccess = {
                            DeleteMyImage(complaint.imageUri, onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Post Deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isDelete.value=false
                                navController.navigate("MyPosts")
                            }, onFailure = {
                                isDelete.value=false
                            })
                        }, onFailure = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            isDelete.value=false
                        })
                    }
                }
            ) {
                if(isDelete.value){
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Deleting...")
                }else{
                    Text(text = "Delete this complaint")
                }
            }
        }
    }
}
