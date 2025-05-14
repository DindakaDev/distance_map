package com.dindaka.distancemap.utils

import java.math.BigDecimal
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class DistanceUtil {
    companion object {
        private const val METERS_IN_MILE = 1609.344
        private const val START_SUBSTRING = 0
        private const val END_SUBSTRING = 8

        fun distanceInMeters(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {
            val theta = lon1 - lon2
            var dist =
                (sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + (cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                    deg2rad(theta)
                )))
            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515
            val distMeters = (dist * METERS_IN_MILE).toString()
            if (distMeters.length > END_SUBSTRING) {
                return BigDecimal(distMeters).toString().substring(START_SUBSTRING, END_SUBSTRING)
                    .toDouble()
            }
            return BigDecimal(distMeters).toString().toDouble()
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }
    }
}