package com.example.ecotrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class HistoryAdapter(
    private var entries: List<WaterEntry>,
    private val onDeleteClicked: (WaterEntry) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAmount: TextView = itemView.findViewById(R.id.tvItemAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvItemDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvItemLocation)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnItemDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_water_entry, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val entry = entries[position]
        holder.tvAmount.text = "${entry.amount} ml"
        holder.tvDate.text = entry.date

        if (entry.latitude != null && entry.longitude != null) {
            holder.tvLocation.text = String.format(
                Locale.getDefault(),
                "📍 Lat: %.4f, Lng: %.4f",
                entry.latitude,
                entry.longitude
            )
        } else {
            holder.tvLocation.text = "📍 Location not recorded"
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClicked(entry)
        }
    }

    override fun getItemCount(): Int = entries.size

    fun updateEntries(newEntries: List<WaterEntry>) {
        this.entries = newEntries
        notifyDataSetChanged()
    }
}
