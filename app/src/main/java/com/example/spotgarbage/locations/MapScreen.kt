package com.example.spotgarbage.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.spotgarbage.locations.LocationData
import com.example.spotgarbage.locations.fetchLocationsRealtime
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
@Composable
fun MapsScreen() {
    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        fetchLocationsRealtime { locations = it }
    }

    LaunchedEffect(locations) {
        if (locations.isNotEmpty()) {
            val bounds = LatLngBounds.Builder()
            locations.forEach { bounds.include(LatLng(it.latitude, it.longitude)) }
            val latLngBounds = bounds.build()


            val districtZoomLevel = 10f
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200))
            cameraPositionState.move(CameraUpdateFactory.zoomTo(districtZoomLevel)) // Set manual zoom
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true),  // Enables blue dot for current location
        cameraPositionState = cameraPositionState
    ) {
        locations.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                title = "Complaint Location",
                snippet = "Lat: ${location.latitude}, Lng: ${location.longitude}"
            )
        }
    }
}
