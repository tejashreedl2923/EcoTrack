package com.example.ecotrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddWaterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var locationHelper: LocationHelper

    private lateinit var etWaterAmount: TextInputEditText
    private lateinit var tvLocationStatus: TextView
    private lateinit var btnSaveWater: MaterialButton
    private lateinit var btnBack: ImageView

    private lateinit var btnPreset100: MaterialButton
    private lateinit var btnPreset250: MaterialButton
    private lateinit var btnPreset500: MaterialButton
    private lateinit var btnPreset750: MaterialButton

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            fetchLocation()
        } else {
            tvLocationStatus.text = getString(R.string.location_unavailable)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_water)

        dbHelper = DatabaseHelper(this)
        locationHelper = LocationHelper(this)

        initViews()
        setupListeners()
        checkLocationPermissionAndFetch()
    }

    private fun initViews() {
        etWaterAmount = findViewById(R.id.etWaterAmount)
        tvLocationStatus = findViewById(R.id.tvLocationStatus)
        btnSaveWater = findViewById(R.id.btnSaveWater)
        btnBack = findViewById(R.id.btnBack)

        btnPreset100 = findViewById(R.id.btnPreset100)
        btnPreset250 = findViewById(R.id.btnPreset250)
        btnPreset500 = findViewById(R.id.btnPreset500)
        btnPreset750 = findViewById(R.id.btnPreset750)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnPreset100.setOnClickListener { etWaterAmount.setText("100") }
        btnPreset250.setOnClickListener { etWaterAmount.setText("250") }
        btnPreset500.setOnClickListener { etWaterAmount.setText("500") }
        btnPreset750.setOnClickListener { etWaterAmount.setText("750") }

        btnSaveWater.setOnClickListener { saveWaterEntry() }
    }

    private fun checkLocationPermissionAndFetch() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            fetchLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun fetchLocation() {
        tvLocationStatus.text = getString(R.string.location_fetching)
        locationHelper.fetchCurrentLocation { lat, lng ->
            currentLatitude = lat
            currentLongitude = lng
            if (lat != null && lng != null) {
                tvLocationStatus.text = String.format(Locale.getDefault(), "Lat: %.4f, Lng: %.4f", lat, lng)
            } else {
                tvLocationStatus.text = getString(R.string.location_unavailable)
            }
        }
    }

    private fun saveWaterEntry() {
        val amountStr = etWaterAmount.text.toString().trim()
        if (amountStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_invalid_amount), Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toIntOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, getString(R.string.msg_invalid_amount), Toast.LENGTH_SHORT).show()
            return
        }

        val dateTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val resultId = dbHelper.insertEntry(amount, dateTimestamp, currentLatitude, currentLongitude)
        if (resultId != -1L) {
            Toast.makeText(this, getString(R.string.msg_entry_saved), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to save entry to database.", Toast.LENGTH_SHORT).show()
        }
    }
}
