package com.example.ecotrack

/**
 * Data model representing a single water consumption entry.
 */
data class WaterEntry(
    val id: Long = 0,
    val amount: Int,
    val date: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
