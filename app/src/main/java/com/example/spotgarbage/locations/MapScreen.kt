package com.example.spotgarbage.ui
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.spotgarbage.locations.LocationData
import com.example.spotgarbage.locations.fetchLocationsRealtime
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapsScreen() {
    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState()
    val locationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status.isGranted) {
        if (!locationPermission.status.isGranted) {
            locationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            fetchLocationsRealtime { locations = it }
        }
    }
    LaunchedEffect(Unit) {
        fetchLocationsRealtime { locations = it }
    }

    LaunchedEffect(locations) {
        if (locations.isNotEmpty()) {
            val bounds = LatLngBounds.Builder()
            locations.forEach { bounds.include(LatLng(it.latitude, it.longitude)) }
            val latLngBounds = bounds.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200))
            cameraPositionState.move(CameraUpdateFactory.zoomTo(10f))
        }
    }

    if (locationPermission.status.isGranted) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
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
}

