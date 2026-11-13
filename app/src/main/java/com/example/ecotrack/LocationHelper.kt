package com.example.ecotrack

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Helper class for retrieving device location using Google Fused Location Provider API.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    /**
     * Fetches current location coordinates. Calls onResult(lat, lng) when available,
     * or onResult(null, null) if permissions are missing or location is unavailable.
     */
    fun fetchCurrentLocation(onResult: (latitude: Double?, longitude: Double?) -> Unit) {
        if (!hasLocationPermission()) {
            onResult(null, null)
            return
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onResult(location.latitude, location.longitude)
                } else {
                    // Fallback to last known location if current location returned null
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                        if (lastLoc != null) {
                            onResult(lastLoc.latitude, lastLoc.longitude)
                        } else {
                            onResult(null, null)
                        }
                    }.addOnFailureListener {
                        onResult(null, null)
                    }
                }
            }.addOnFailureListener {
                onResult(null, null)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            onResult(null, null)
        }
    }
}
