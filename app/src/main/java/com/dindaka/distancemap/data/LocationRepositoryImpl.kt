package com.dindaka.distancemap.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import com.dindaka.distancemap.domain.LocationRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepositoryImpl(private val context: Context) : LocationRepository {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<LatLng?> = callbackFlow {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            trySend(null)
            close()
            return@callbackFlow
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                trySend(LatLng(it.latitude, it.longitude))
            } ?: trySend(null)
            close()
        }

        awaitClose { /* nothing to clean */ }
    }
}