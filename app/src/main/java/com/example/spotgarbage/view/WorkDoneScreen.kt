package com.example.spotgarbage.view
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.spotgarbage.ui.theme.secondary
import com.example.spotgarbage.viewmodel.ComplaintViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WorkDoneScreen(complaint: Complaint?,navController: NavController) {
    val scrollState = rememberScrollState()
    val uid= FirebaseAuth.getInstance().currentUser?.uid
    val coroutineScope= rememberCoroutineScope()
    val complaintViewModel= ComplaintViewModel()
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
            }

                val isUpdate= remember { mutableStateOf(false) }
            var showDialog = remember { mutableStateOf(false) }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                enabled = !isUpdate.value,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = secondary),
                onClick = {
                    showDialog.value = true
                }
            ) {
                Text(text="Garbage collected")
                if(isUpdate.value){
                    navController.navigate("LottieAnimation")
                }
            }
            if (showDialog.value) {
                AlertDialog(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp),
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Confirm Update") },
                    text = { Text("Are you sure the garbage is collected?") },

                    confirmButton = {
                        TextButton(onClick = {
                            showDialog.value = false
                            isUpdate.value = true
                            coroutineScope.launch {
                                UpdateMyComplaint(complaintViewModel, complaint) { result ->
                                    if (result == "Success") {
                                        isUpdate.value = false
                                        Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show()
                                        navController.navigate("adminHome") {
                                            popUpTo(0)
                                        }
                                    } else {
                                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                                        isUpdate.value = false
                                    }
                                }
                            }
                        }, modifier = Modifier.padding(start = 10.dp,end=10.dp)) {
                            Text("Yes, Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }


        }
    }
}

@SuppressLint("NewApi")
fun UpdateMyComplaint(complaintViewModel: ComplaintViewModel, complaint: Complaint, onResult: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    if (complaint.postId.isNullOrEmpty()) {
        onResult("Error: Invalid postId")
        return
    }
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate = currentDate.format(formatter)
    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
    val currentTime = LocalTime.now()
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val formattedTime = currentTime.format(timeFormatter)
    db.collection("complaints").document(complaint.postId)
        .update(
            "status", "Done",
            "clearedOn", "${dayOfWeek}, ${formattedDate} at ${formattedTime}"
        )
        .addOnSuccessListener {
            Log.d("FirestoreUpdate", "Update successful for ${complaint.postId}")
            complaintViewModel.fetchPosts()
            onResult("Success")
        }
        .addOnFailureListener { e ->
            Log.e("FirestoreUpdateError", "Update failed: ${e.message}")
            onResult(e.message ?: "Unknown error occurred")
        }

}




