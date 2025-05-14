package com.dindaka.distancemap.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatLngDto(
    val lat: Double,
    val lng: Double
): Parcelable