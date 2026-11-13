package com.example.ecotrack

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: HistoryAdapter

    private lateinit var tvSummaryEntriesCount: TextView
    private lateinit var tvSummaryTotalVolume: TextView
    private lateinit var layoutEmptyHistory: LinearLayout
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DatabaseHelper(this)

        initViews()
        setupRecyclerView()
        loadHistoryData()
    }

    private fun initViews() {
        rvHistory = findViewById(R.id.rvHistory)
        tvSummaryEntriesCount = findViewById(R.id.tvSummaryEntriesCount)
        tvSummaryTotalVolume = findViewById(R.id.tvSummaryTotalVolume)
        layoutEmptyHistory = findViewById(R.id.layoutEmptyHistory)
        btnBack = findViewById(R.id.btnHistoryBack)

        btnBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        rvHistory.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList()) { entry ->
            confirmDeleteEntry(entry)
        }
        rvHistory.adapter = adapter
    }

    private fun loadHistoryData() {
        val entries = dbHelper.getAllEntries()
        adapter.updateEntries(entries)

        val totalVolume = entries.sumOf { it.amount }
        tvSummaryEntriesCount.text = entries.size.toString()
        tvSummaryTotalVolume.text = getString(R.string.amount_ml_format, totalVolume)

        if (entries.isEmpty()) {
            layoutEmptyHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.GONE
        } else {
            layoutEmptyHistory.visibility = View.GONE
            rvHistory.visibility = View.VISIBLE
        }
    }

    private fun confirmDeleteEntry(entry: WaterEntry) {
        AlertDialog.Builder(this)
            .setTitle(R.string.btn_delete)
            .setMessage(R.string.delete_entry_confirm)
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                val success = dbHelper.deleteEntry(entry.id)
                if (success) {
                    Toast.makeText(this, R.string.msg_entry_deleted, Toast.LENGTH_SHORT).show()
                    loadHistoryData()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }
}
