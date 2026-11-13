package com.example.ecotrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var locationHelper: LocationHelper

    private lateinit var tvTodayConsumption: TextView
    private lateinit var tvDailyGoalSub: TextView
    private lateinit var tvRemainingWater: TextView
    private lateinit var tvProgressPercentage: TextView
    private lateinit var progressBarWater: ProgressBar
    private lateinit var cardGoalAchieved: MaterialCardView

    private lateinit var btnQuick100: MaterialButton
    private lateinit var btnQuick250: MaterialButton
    private lateinit var btnQuick500: MaterialButton
    private lateinit var btnQuick750: MaterialButton

    private lateinit var cardNavAddWater: MaterialCardView
    private lateinit var cardNavHistory: MaterialCardView
    private lateinit var cardNavMap: MaterialCardView
    private lateinit var cardNavSettings: MaterialCardView
    private lateinit var btnHeaderSettings: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        locationHelper = LocationHelper(this)

        initViews()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun initViews() {
        tvTodayConsumption = findViewById(R.id.tvTodayConsumption)
        tvDailyGoalSub = findViewById(R.id.tvDailyGoalSub)
        tvRemainingWater = findViewById(R.id.tvRemainingWater)
        tvProgressPercentage = findViewById(R.id.tvProgressPercentage)
        progressBarWater = findViewById(R.id.progressBarWater)
        cardGoalAchieved = findViewById(R.id.cardGoalAchieved)

        btnQuick100 = findViewById(R.id.btnQuick100)
        btnQuick250 = findViewById(R.id.btnQuick250)
        btnQuick500 = findViewById(R.id.btnQuick500)
        btnQuick750 = findViewById(R.id.btnQuick750)

        cardNavAddWater = findViewById(R.id.cardNavAddWater)
        cardNavHistory = findViewById(R.id.cardNavHistory)
        cardNavMap = findViewById(R.id.cardNavMap)
        cardNavSettings = findViewById(R.id.cardNavSettings)
        btnHeaderSettings = findViewById(R.id.btnHeaderSettings)
    }

    private fun setupListeners() {
        btnQuick100.setOnClickListener { quickAddWater(100) }
        btnQuick250.setOnClickListener { quickAddWater(250) }
        btnQuick500.setOnClickListener { quickAddWater(500) }
        btnQuick750.setOnClickListener { quickAddWater(750) }

        cardNavAddWater.setOnClickListener {
            startActivity(Intent(this, AddWaterActivity::class.java))
        }

        cardNavHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        cardNavMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        cardNavSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnHeaderSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateDashboard() {
        val sharedPref = getSharedPreferences("EcoTrackPrefs", Context.MODE_PRIVATE)
        val dailyGoal = sharedPref.getInt("daily_goal", 2000)

        val todayTotal = dbHelper.getTodayTotalAmount()
        val remaining = maxOf(0, dailyGoal - todayTotal)
        val percentage = if (dailyGoal > 0) minOf(100, (todayTotal * 100) / dailyGoal) else 0

        tvTodayConsumption.text = todayTotal.toString()
        tvDailyGoalSub.text = getString(R.string.daily_goal_sub_format, dailyGoal)
        tvRemainingWater.text = getString(R.string.amount_ml_format, remaining)
        tvProgressPercentage.text = "$percentage%"
        progressBarWater.progress = percentage

        if (todayTotal >= dailyGoal && dailyGoal > 0) {
            cardGoalAchieved.visibility = View.VISIBLE
        } else {
            cardGoalAchieved.visibility = View.GONE
        }
    }

    private fun quickAddWater(amount: Int) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        locationHelper.fetchCurrentLocation { lat, lng ->
            val rowId = dbHelper.insertEntry(amount, timestamp, lat, lng)
            if (rowId != -1L) {
                Toast.makeText(
                    this,
                    "Added $amount ml water entry!",
                    Toast.LENGTH_SHORT
                ).show()
                updateDashboard()
            } else {
                Toast.makeText(this, "Failed to record water entry.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
