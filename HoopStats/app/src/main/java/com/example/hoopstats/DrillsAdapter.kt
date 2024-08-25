package com.example.hoopstats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hoopstats.data.Drill
import com.example.hoopstats.databinding.ItemDrillBinding
import java.text.SimpleDateFormat

class DrillsAdapter(private val drills: List<Drill>, private val listener: OnItemClickListener) : RecyclerView.Adapter<DrillsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemDrillBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return drills.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(drills[position])
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    inner class ViewHolder(private val binding: ItemDrillBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(drill: Drill) {
            with(binding) {
                textViewDrillName.text = drill.name ?: "--"
                textViewLastDone.text = drill.lastDone?.let {
                    SimpleDateFormat("yyyy-MM-dd").format(
                        it
                    )
                }
                    ?: "Never"
                textViewPercentage.text = "${drill.percentage}%"
                drill.image?.let { imageViewDrillIcon.setImageResource(it) }
            }
        }
    }
}