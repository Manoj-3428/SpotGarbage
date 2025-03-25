package com.example.spotgarbage.view
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotgarbage.R
import com.example.spotgarbage.firebase.getAddressFromLocation
import com.example.spotgarbage.firebase.saveDataToFirebase
import com.example.spotgarbage.ui.theme.*
import com.example.spotgarbage.viewmodel.GarbageDetectionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MissingPermission", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun addComplaint(navController: NavController) {
    val types = listOf("Plastic", "Organic", "Paper", "Glass", "Metal", "Other")
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val expanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(types[0]) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val locationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPermission=rememberPermissionState(android.Manifest.permission.CAMERA)
    val address = remember { mutableStateOf("") }
    val latitude = remember { mutableStateOf<Double?>(null) }
    val longitude = remember { mutableStateOf<Double?>(null) }
    val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val description = remember { mutableStateOf("") }
    val isGetting=remember { mutableStateOf(false) }
    val posted=remember { mutableStateOf(false) }
    var detectionResult=""
    var tempFile = remember { File(context.cacheDir, "${System.currentTimeMillis()}.jpg") }
    val notificationPermission=rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    val viewModel = GarbageDetectionViewModel()
    val testUri = getImageUriFromDrawable(context, R.drawable.garbage)
    val keyboardController= LocalSoftwareKeyboardController.current

    var tempUri = remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri.value = tempUri
        }
    }
    val singleImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value=uri
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(top=30.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(250.dp)
                .padding(start = 10.dp, end = 10.dp, top = 20.dp).border(2.dp,black)
                .clickable {
//                    if(cameraPermission.status.isGranted){
//                        val newTempFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
//                        val newTempUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", newTempFile)
//                        tempFile.delete()
//
//                        tempFile = newTempFile
//                        tempUri = newTempUri
//                        cameraLauncher.launch(tempUri)
//                    }
//                    else{
//                        cameraPermission.launchPermissionRequest()
//                    }
                    singleImageLauncher.launch("image/*")
                           },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.value != null) {
                AsyncImage(
                    model = imageUri.value,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize().height(250.dp).fillMaxWidth().padding(), contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.cam),
                    contentDescription = "Placeholder Image",
                    modifier = Modifier.fillMaxSize(),
                    )
            }
        }
        Spacer(modifier=Modifier.height(10.dp))
        Text(
            text = "Fill complaint details",
            color = secondary,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier=Modifier.height(10.dp))
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it },
        ) {
            OutlinedTextField(
                value = selectedOption.value,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Select the type of waste", color = Color.Gray) },

                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "arrow") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .menuAnchor()
                    .clickable { expanded.value = !expanded.value },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = secondary_light,
                    unfocusedLabelColor = secondary,
                    focusedBorderColor = secondary_light,
                    unfocusedBorderColor = secondary,
                    focusedTrailingIconColor = secondary_light,
                    unfocusedTrailingIconColor = secondary
                )
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedOption.value = type
                            expanded.value = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = address.value,
            onValueChange = { address.value = it },

            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 10.dp),
            label = { Text(
                text = if (isGetting.value) "Fetching location..."

                else "Click the icon for accurate current location",color = if (isGetting.value) Color.Gray else secondary
                ) },
            trailingIcon = {
                if (isGetting.value) {
                    CircularProgressIndicator(
                        color = primary_dark,
                        modifier = Modifier.size(24.dp)
                    )
                } else{
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "location",
                        modifier = Modifier.clickable {
                            isGetting.value = true
                            if (locationPermission.status.isGranted) {
                                fusedLocation.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    null
                                )
                                    .addOnSuccessListener { loc ->
                                        if (loc != null) {
                                            latitude.value = loc.latitude
                                            longitude.value = loc.longitude
                                            coroutineScope.launch(Dispatchers.IO) {
                                                address.value = getAddressFromLocation(
                                                    latitude.value ?: 0.0,
                                                    longitude.value ?: 0.0,
                                                    context
                                                ) + "\n" + "Latitude:${latitude.value}" + "\n" + "Longitude:${longitude.value}"
                                                isGetting.value = false
                                            }
                                        } else {
                                            address.value = "Address not found"
                                            isGetting.value = false
                                        }
                                    }.addOnFailureListener {
                                        address.value = it.message.toString()
                                        isGetting.value = false

                                    }
                            } else {
                                locationPermission.launchPermissionRequest()
                            }
                        })
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = secondary_light,
                unfocusedLabelColor = Color.Gray,
                focusedBorderColor = secondary_light,
                unfocusedBorderColor = secondary,
                focusedTrailingIconColor = secondary_light,
                unfocusedTrailingIconColor = secondary
            )
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Detail description about the complaint") },
            modifier = Modifier.fillMaxWidth().height(150.dp).padding(10.dp),
            minLines = 3,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = secondary_light,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = secondary_light,
                unfocusedLabelColor = secondary,
                focusedLeadingIconColor = secondary_light,
                unfocusedLeadingIconColor = secondary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier=Modifier.weight(1f))
        val isLoading = remember { mutableStateOf(false) }
        if(isGetting.value==false) {
            Button(

                onClick = {
                    coroutineScope.launch {
                        imageUri.value?.let {
                            isLoading.value = true
                            notificationPermission.launchPermissionRequest()
                            val filePath = getRealPathFromUri(context, imageUri.value!!)
                            viewModel.detectGarbage(filePath, "A1AJq42TntoRtDVu5VBg") { result ->
                                detectionResult = result
                                if (detectionResult == "No garbage detected" || detectionResult.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "No garbage detected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isLoading.value = false
                                    navController.navigate("addComplaint"){
                                        popUpTo(0)
                                    }
                                    showSomeNotification(context, detectionResult, false)
                                } else {
                                    saveDataToFirebase(
                                        detectionResult,
                                        address.value,
                                        notificationPermission.status.isGranted,
                                        it,
                                        context,
                                        selectedOption.value,
                                        description.value,
                                        address.value,
                                        latitude.value.toString(),
                                        longitude.value.toString()

                                    ) {
                                        isLoading.value = false
                                        navController.navigate("Home"){
                                            popUpTo(0)
                                        }
                                        Toast.makeText(
                                            context,
                                            "Detection Result is ${detectionResult}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } ?: Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                enabled = !isLoading.value,
                modifier = Modifier.padding(top = 20.dp, end = 20.dp, start = 20.dp, bottom = 15.dp)
                    .fillMaxWidth().align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary_light,
                    contentColor = white
                )
            ) {
                Text(text="Post")
            }
            if(isLoading.value){
                navController.navigate("LottieAnimation")
            }
        }
    }
}
fun getRealPathFromUri(context: Context, uri: Uri): String {
    val file = File(context.cacheDir, "temp_image.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output) // Copies the file
        }
    }
    return file.absolutePath
}
@SuppressLint("MissingPermission", "NotificationPermission")
fun showSomeNotification(context: Context,data: String?,status:Boolean) {
    val channelId = "simple_channel"
    val channelName = "simple"
    var channelDescription = "Nothing simple"
    val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = channelDescription
            setSound(soundUri, audioAttributes)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }
    val content = if (!status) {
        "We are unable to find the garbage in that image. Please try to take another capture"
    } else {

    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.app_icon)
        .setContentTitle("No Garbage Detected!!")
        .setStyle(NotificationCompat.BigTextStyle().bigText("We are unable to find the garbage in that image. Please try to take another capture"))
        .setSound(soundUri)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL)

    NotificationManagerCompat.from(context).notify(1, builder.build())

}
fun getImageUriFromDrawable(context: Context, drawableId: Int): Uri? {
    val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
    val file = File(context.cacheDir, "test_image.png")

    FileOutputStream(file).use { outputStream ->
        val bitmap = (drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

