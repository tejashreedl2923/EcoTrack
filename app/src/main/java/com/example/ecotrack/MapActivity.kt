package com.example.ecotrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var locationHelper: LocationHelper

    private lateinit var tvMapLocationsCount: TextView
    private lateinit var tvMapEmptyState: TextView
    private lateinit var btnBack: ImageView

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        dbHelper = DatabaseHelper(this)
        locationHelper = LocationHelper(this)

        initViews()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun initViews() {
        tvMapLocationsCount = findViewById(R.id.tvMapLocationsCount)
        tvMapEmptyState = findViewById(R.id.tvMapEmptyState)
        btnBack = findViewById(R.id.btnMapBack)

        btnBack.setOnClickListener { finish() }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (locationHelper.hasLocationPermission()) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap?.isMyLocationEnabled = true
            }
        }

        googleMap?.uiSettings?.isZoomControlsEnabled = true

        displayLocationMarkers()
    }

    private fun displayLocationMarkers() {
        val allEntries = dbHelper.getAllEntries()
        val locationEntries = allEntries.filter { it.latitude != null && it.longitude != null }

        tvMapLocationsCount.text = "${locationEntries.size} Locations Saved"

        if (locationEntries.isEmpty()) {
            tvMapEmptyState.visibility = View.VISIBLE
            // Default center if no points (e.g. Center of India or world default)
            val defaultPoint = LatLng(20.5937, 78.9629)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPoint, 5f))
            return
        }

        tvMapEmptyState.visibility = View.GONE
        val boundsBuilder = LatLngBounds.Builder()

        for (entry in locationEntries) {
            val lat = entry.latitude!!
            val lng = entry.longitude!!
            val position = LatLng(lat, lng)

            boundsBuilder.include(position)

            googleMap?.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("💧 ${entry.amount} ml Intake")
                    .snippet("Time: ${entry.date}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        }

        if (locationEntries.size == 1) {
            val singlePos = LatLng(locationEntries[0].latitude!!, locationEntries[0].longitude!!)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(singlePos, 15f))
        } else {
            val bounds = boundsBuilder.build()
            val padding = 120 // px padding around markers
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }
}
