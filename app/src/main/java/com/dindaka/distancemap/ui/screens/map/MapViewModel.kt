package com.dindaka.distancemap.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dindaka.distancemap.domain.LocationRepository
import com.dindaka.distancemap.utils.DistanceUtil
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {
    private val _location = MutableStateFlow<LatLng?>(null)
    val location: StateFlow<LatLng?> = _location

    private val _markers = MutableStateFlow<List<LatLng>?>(null)
    val markers: StateFlow<List<LatLng>?> = _markers

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            repository.getCurrentLocation().collect {
                _location.value = it
            }
        }
    }

    fun toogleMarker(position: LatLng) {
        val currentMarkers = _markers.value?.toMutableList()
        val existing = currentMarkers?.find { it.isCloseTo(position) }
        if (existing != null) {
            currentMarkers.remove(existing)
        } else {
            currentMarkers?.add(position)
        }
        _markers.value = sorterByLatLng(currentMarkers?.toList() ?: emptyList())
    }

    fun removeMarker(position: LatLng) {
        val currentMarkers = _markers.value?.toMutableList()
        currentMarkers?.removeIf { it.isCloseTo(position) }
        _markers.value = sorterByLatLng(currentMarkers?.toList() ?: emptyList())
    }

    private fun LatLng.isCloseTo(other: LatLng, threshold: Double = 0.0001): Boolean {
        return abs(latitude - other.latitude) < threshold &&
                abs(longitude - other.longitude) < threshold
    }

    fun removeMarkerNearby() {
        val distByMarker = returnPedidoMoreNearby()
        removeMarker(distByMarker)
    }

    fun returnPedidoMoreNearby(): LatLng {
        val sorterItems = sorterByLatLng(markers.value ?: emptyList())
        return sorterItems.first()
    }

    fun sorterByLatLng(items: List<LatLng>): List<LatLng> {
        val distByMarker = items.map {
            val dist = DistanceUtil.distanceInMeters(
                lon1 = location.value?.longitude ?: 0.0,
                lat1 = location.value?.latitude ?: 0.0,
                lat2 = it.latitude,
                lon2 = it.longitude
            )
            it to dist
        }.sortedBy { it.second }
        return distByMarker.map { it.first }.toList()
    }
}