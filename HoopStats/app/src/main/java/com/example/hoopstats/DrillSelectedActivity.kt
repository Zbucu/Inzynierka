package com.example.hoopstats

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hoopstats.data.Drill
import com.example.hoopstats.data.DrillSession
import com.example.hoopstats.data.HoopStatsDatabase
import com.example.hoopstats.databinding.ActivityDrillSelectedBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.concurrent.thread

class DrillSelectedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrillSelectedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrillSelectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentDate = Calendar.getInstance()
        val defaultStartDate = currentDate.clone() as Calendar
        defaultStartDate.add(Calendar.DAY_OF_YEAR, -7)
        defaultStartDate.set(Calendar.HOUR_OF_DAY, 0) // Set hour to 0 (midnight)
        defaultStartDate.set(Calendar.MINUTE, 1) // Set minute to 1// Subtract 7 days from current date

        val defaultEndDate = currentDate.clone() as Calendar
        defaultEndDate.set(Calendar.HOUR_OF_DAY, 23)
        defaultEndDate.set(Calendar.MINUTE, 59)


        val drillId = intent.getIntExtra("drillId", -1)
        if (drillId != -1) {
            // Launch a new thread to fetch the drill from the database
            thread {
                val database = HoopStatsDatabase.getDatabase(this)
                val drillDao = database.getDrillDao()
                val selectedDrill = drillDao.getDrillById(drillId)
                val drillSessionDao = database.getDrillSessionDao()
                var sessions = drillSessionDao.getAllSessionsForDrillBetweenDates(drillId, defaultStartDate.time, defaultEndDate.time)



                runOnUiThread {
                    if (selectedDrill != null) {
                        // Update UI with the selected drill
                        updateUI(selectedDrill)

                        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_drill_sessions)
                        val drillSessionsAdapter = DrillSessionsAdapter(sessions.reversed())
                        recyclerView.adapter = drillSessionsAdapter


                        val buttonStartDrill = findViewById<Button>(R.id.button_start_drill)

                        // Set OnClickListener to the button
                        buttonStartDrill.setOnClickListener {
                            // Create an Intent to start the DrillActivity
                            val intent = Intent(this, DrillActivity::class.java)
                            intent.putExtra("drillId", drillId)
                            // Start the DrillActivity
                            startActivity(intent)
                        }
                    } else {
                        // Handle case where drill is not found
                        Toast.makeText(this, "Drill not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Invalid drill ID", Toast.LENGTH_SHORT).show()
        }

        // In your activity or fragment
        val startDateEditText = findViewById<EditText>(R.id.edit_text_date_start)
        val endDateEditText = findViewById<EditText>(R.id.edit_text_date_end)



        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        startDateEditText.setText(dateFormat.format(defaultStartDate.time))
        endDateEditText.setText(dateFormat.format(defaultEndDate.time))

// Set OnClickListener for start date EditText
        startDateEditText.setOnClickListener {
            showDatePickerDialog(startDateEditText)
        }

// Set OnClickListener for end date EditText
        endDateEditText.setOnClickListener {
            showDatePickerDialog(endDateEditText)
        }

        val refreshButton = findViewById<ImageButton>(R.id.button_refresh)

        refreshButton.setOnClickListener {
            refreshSessionsList()
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun updateUI(drill: Drill) {
        // Update UI with the selected drill
        binding.textViewDrillName.text = drill.name
        binding.textViewLastDone.text = drill.lastDone?.let {
            SimpleDateFormat("dd-MM-yyyy").format(
                it
            )
        } ?: "Never"
        binding.textViewPercentage.text = "${drill.percentage}%"
        binding.imageViewDrillIcon.setImageResource(drill.image)
        // Update other UI elements accordingly
    }

    fun showDatePickerDialog(editText: EditText) {
        val datePickerFragment = DatePickerFragment().apply {
            setEditText(editText)
        }
        datePickerFragment.show(supportFragmentManager, "datePicker")
    }

    fun refreshSessionsList() {
        val startDateEditText = findViewById<EditText>(R.id.edit_text_date_start)
        val endDateEditText = findViewById<EditText>(R.id.edit_text_date_end)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        var startDate = dateFormat.parse(startDateEditText.text.toString())
        var endDate = dateFormat.parse(endDateEditText.text.toString())

        val calendarStart = Calendar.getInstance()
        calendarStart.time = startDate

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = endDate

// Set start date time to 00:01
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 1)

// Set end date time to 23:59
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        calendarEnd.set(Calendar.MINUTE, 59)

// Update the startDate and endDate with the modified time
        startDate = calendarStart.time
        endDate = calendarEnd.time
        // Perform database query with the selected dates
        thread {
            val database = HoopStatsDatabase.getDatabase(this)
            val drillId = intent.getIntExtra("drillId", -1)
            val drillSessionDao = database.getDrillSessionDao()
            var sessions = drillSessionDao.getAllSessionsForDrillBetweenDates(drillId, startDate, endDate)

            runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_drill_sessions)
                val drillSessionsAdapter = DrillSessionsAdapter(sessions.reversed())
                recyclerView.adapter = drillSessionsAdapter
            }
        }
    }
}
