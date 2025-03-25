package com.example.spotgarbage.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.spotgarbage.R
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.dataclasses.Profiles
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime
@RequiresApi(Build.VERSION_CODES.O)
fun saveDataToFirebase(
    detectionResult: String, address: String, isGranted: Boolean, uri: Uri, context: Context,
    type: String, description: String, location: String, latitude: String, longitude: String,
    onComplete: () -> Unit
) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate = currentDate.format(formatter)
    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
    val currentTime = LocalTime.now()
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val formattedTime = currentTime.format(timeFormatter)
    val userid = FirebaseAuth.getInstance().currentUser?.uid
    var username = ""
    var email = ""
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    if (userid == null) {
        Log.e("FirebaseError", "User not authenticated")
        return
    }

    db.collection("users").document(userid).get().addOnSuccessListener { document ->
        val profile = document.toObject(Profiles::class.java)
        if (profile != null) {
            username = profile.name
            email = profile.email
        }
    }

    val postId = "${userid}_${System.currentTimeMillis()}"
    val storageReference = storage.reference.child("complaints_images/$postId.jpg")

    Log.d("UploadDebug", "Starting image compression for postId: $postId")
    val compressedImageByteArray = compressImage(uri, context)
    if (compressedImageByteArray != null) {
        val uploadTask = storageReference.putBytes(compressedImageByteArray)

        uploadTask.addOnSuccessListener {
            Log.d("UploadDebug", "File uploaded successfully")
            storageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
                Log.d("UploadDebug", "Download URL received: ${downloadUrl.toString()}")
                storeComplaints(
                    detectionResult, userid, isGranted, postId, db, address, downloadUrl.toString(),
                    context, type, description, location, latitude, longitude, formattedDate,
                    dayOfWeek, formattedTime, username, email, onComplete
                )
            }.addOnFailureListener {
                Log.e("UploadError", "Failed to get download URL: ${it.message}")
                onComplete()
            }
        }.addOnFailureListener {
            Log.e("UploadError", "File upload failed: ${it.message}")
            onComplete()
        }
    } else {
        Log.e("CompressionError", "Image compression failed")
        onComplete()
    }
}

fun compressImage(uri: Uri, context: Context): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        outputStream.toByteArray()
    } catch (e: Exception) {
        Log.e("CompressionError", "Failed to compress image: ${e.message}")
        null
    }
}


fun storeComplaints(detectionResults: String, userid: String, isGranted: Boolean, postId: String, db: FirebaseFirestore,
                    address: String, imageUri: String, context:
                    Context, type: String, description: String, location: String,
                    latitude: String, longitude: String,
                    formattedDate: String, dayOfWeek: String, formattedTime: String,
                    username: String?, email:String?, onComplete: () -> Unit) {
    if (userid != null) {
        val timestamp = Timestamp.now()
        val complaint = Complaint(userid,timestamp,
            username.toString(), postId, address, imageUri, type, description, location, latitude, longitude, formattedDate, dayOfWeek, formattedTime,email.toString())
        db.collection("complaints").document(postId).set(complaint).addOnSuccessListener {
            Toast.makeText(context, "Complaint saved", Toast.LENGTH_SHORT).show()

            if(isGranted) {
                showNotification(detectionResults,context,username)
            }
            onComplete()
        }.addOnFailureListener {
            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
            onComplete()
        }
    }
}
@SuppressLint("MissingPermission", "NotificationPermission")
fun showNotification(detectionResults: String,context: Context,username: String?) {
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
    val content = "$username, your efforts in keeping the environment clean are truly inspiring! 🌍💚 Keep making a difference! 😊"
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.app_icon)
        .setContentTitle("Thank You, $username! 🎉")
        .setContentText("Your complaint has been successfully recorded.")
        .setStyle(NotificationCompat.BigTextStyle().bigText("Your complaint has been successfully recorded. We got $detectionResults"))
        .setSound(soundUri)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL)

    NotificationManagerCompat.from(context).notify(1, builder.build())

}
