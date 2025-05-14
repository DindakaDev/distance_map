package com.dindaka.distancemap.ui.screens.map

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dindaka.distancemap.R
import com.dindaka.distancemap.data.remote.dto.LatLngDto
import com.dindaka.distancemap.ui.navigation.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isMapLoaded by remember { mutableStateOf(false) }
    val location by viewModel.location.collectAsState()
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val markers by viewModel.markers.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status.isGranted) {
            viewModel.fetchCurrentLocation()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Pedidos",
                    style = MaterialTheme.typography.titleLarge,
                )
            })
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    onMapLoaded = { isMapLoaded = true },
                    uiSettings = MapUiSettings(
                        compassEnabled = true,
                        zoomControlsEnabled = true,
                        zoomGesturesEnabled = true,
                        myLocationButtonEnabled = true,
                        rotationGesturesEnabled = true,
                    ),
                    properties = MapProperties(
                        isMyLocationEnabled = true
                    ),
                    onMapClick = { latLng -> viewModel.toogleMarker(latLng) }
                ) {
                    if (locationPermission.status.isGranted) {
                        markers?.forEachIndexed { pos, latLng ->
                            Marker(
                                state = MarkerState(position = latLng),
                                icon = BitmapDescriptorFactory.defaultMarker(if (pos == 0) BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_BLUE),
                                title = "Marcador: ${latLng.latitude},${latLng.longitude}",
                                onClick = {
                                    viewModel.removeMarker(latLng)
                                    true
                                }
                            )
                        }
                    }
                }
                if (!locationPermission.status.isGranted) {
                    Button(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        onClick = { locationPermission.launchPermissionRequest() }) {
                        Text(stringResource(R.string.permission_ubication))
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if ((markers?: emptyList()).size > 1) {
                            Button(
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "markers",
                                        markers?.map { LatLngDto(it.latitude, it.longitude) }?.toList())
                                    navController.navigate(Routes.List.route)
                                }) {
                                Text(stringResource(R.string.show_list_by_ubication))
                            }
                        }
                        if (!markers.isNullOrEmpty()) {
                            Button(
                                onClick = {
                                    viewModel.removeMarkerNearby()
                                }
                            ) {
                                Text(stringResource(R.string.entregar_cercano))
                            }
                        }
                    }
                }
            }

        }
    )
}