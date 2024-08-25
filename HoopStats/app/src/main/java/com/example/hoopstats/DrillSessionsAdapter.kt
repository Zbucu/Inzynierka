package com.example.hoopstats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hoopstats.data.Drill
import com.example.hoopstats.data.DrillSession
import com.example.hoopstats.databinding.ItemDrillSessionBinding
import java.text.SimpleDateFormat

class DrillSessionsAdapter(private val sessions: List<DrillSession>) : RecyclerView.Adapter<DrillSessionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDrillSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sessions[position])
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    inner class ViewHolder(private val binding: ItemDrillSessionBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(session: DrillSession) {
            with(binding) {
                textViewDate.text = SimpleDateFormat("yyyy-MM-dd").format(session.date)
                textViewPercentage.text = "${session.percentage}%"
                textViewShotsMadeTaken.text = "${session.shotsMade}/${session.shotsTaken}"
            }
        }
    }
}