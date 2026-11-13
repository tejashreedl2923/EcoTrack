package com.example.ecotrack

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etDailyGoal: TextInputEditText
    private lateinit var btnSaveGoal: MaterialButton
    private lateinit var btnResetHistory: MaterialButton
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        dbHelper = DatabaseHelper(this)

        initViews()
        loadCurrentGoal()
        setupListeners()
    }

    private fun initViews() {
        etDailyGoal = findViewById(R.id.etDailyGoal)
        btnSaveGoal = findViewById(R.id.btnSaveGoal)
        btnResetHistory = findViewById(R.id.btnResetHistory)
        btnBack = findViewById(R.id.btnSettingsBack)
    }

    private fun loadCurrentGoal() {
        val sharedPref = getSharedPreferences("EcoTrackPrefs", Context.MODE_PRIVATE)
        val currentGoal = sharedPref.getInt("daily_goal", 2000)
        etDailyGoal.setText(currentGoal.toString())
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnSaveGoal.setOnClickListener {
            val goalStr = etDailyGoal.text.toString().trim()
            val goal = goalStr.toIntOrNull()

            if (goal == null || goal <= 0) {
                Toast.makeText(this, "Please enter a valid daily water goal in ml.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("EcoTrackPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("daily_goal", goal)
                apply()
            }

            Toast.makeText(
                this,
                getString(R.string.msg_goal_updated, goal),
                Toast.LENGTH_SHORT
            ).show()
        }

        btnResetHistory.setOnClickListener {
            confirmResetHistory()
        }
    }

    private fun confirmResetHistory() {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_reset_title)
            .setMessage(R.string.confirm_reset_msg)
            .setPositiveButton(R.string.btn_reset_history) { _, _ ->
                val cleared = dbHelper.clearAllEntries()
                if (cleared) {
                    Toast.makeText(this, R.string.msg_history_reset, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }
}
