package com.dindaka.distancemap.ui.screens.list

import android.Manifest
import android.widget.ListView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dindaka.distancemap.R
import com.dindaka.distancemap.data.remote.dto.LatLngDto
import com.dindaka.distancemap.ui.components.LocationPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import kotlin.text.*

@Composable
fun ListScreen(
    navController: NavController,
    markers: List<LatLngDto>,
    viewModel: ListViewModel = hiltViewModel()
) {
    var hasPermission by remember { mutableStateOf(false) }

    if (hasPermission) {
        BodyWidget(navController, viewModel)
    } else {
        LocationPermission(onPermissionGranted = {
            hasPermission = true
        })
    }
    viewModel.setMarkers(markers)
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BodyWidget(navController: NavController, viewModel: ListViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(R.string.listado_de_pedidos),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CurrentLocationWidget(viewModel)
                MarkerListSorter(viewModel)
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MarkerListSorter(viewModel: ListViewModel) {
    val location by viewModel.location.collectAsState()
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status.isGranted) {
            viewModel.fetchCurrentLocation()
        }
    }
    val markers = viewModel.returnListWithDistance(location)
    if (markers != null) {
        Column {
            SectionTitle(stringResource(R.string.pedidos))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(markers) {
                    MarkerItemWidget(it)
                }

            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun MarkerItemWidget(item: Pair<LatLng, Double>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.posici_n),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lat: ${item.first.latitude}, Lng: ${item.first.longitude}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.distancia),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${String.format("%.2f", item.second)} mt",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentLocationWidget(viewModel: ListViewModel) {
    val location by viewModel.location.collectAsState()
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status.isGranted) {
            viewModel.fetchCurrentLocation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.posicion_actual),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Lat: ${location?.latitude ?: 0.0}, Lng: ${location?.longitude ?: 0.0}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}