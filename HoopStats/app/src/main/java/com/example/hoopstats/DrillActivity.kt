package com.example.hoopstats

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.hoopstats.data.Drill
import com.example.hoopstats.data.DrillSession
import com.example.hoopstats.data.HoopStatsDatabase
import com.example.hoopstats.databinding.ActivityDrillBinding
import java.time.LocalDate
import java.util.Calendar
import kotlin.concurrent.thread


class DrillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrillBinding
    private lateinit var database: HoopStatsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrillBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = HoopStatsDatabase.getDatabase(this)

        val drillId = intent.getIntExtra("drillId", -1) // Default value in case of missing extra

        if (drillId != -1) {
            thread {
                val drill = database.getDrillDao().getDrillById(drillId)
                runOnUiThread {
                    if (drill != null) {
                        // Do something with the retrieved Drill object
                        // For example, set the text of a TextView with the drill name
                        val drillNameTextView = findViewById<TextView>(R.id.drill_name)

                       drillNameTextView.text = drill.name
                    } else {
                        // Handle the case where no Drill object is found for the provided drillId
                        Toast.makeText(this@DrillActivity, "No drill found for ID: $drillId", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Handle the case where drillId is not passed correctly
            Toast.makeText(this, "Invalid drill ID", Toast.LENGTH_SHORT).show()
        }

        if (!hasPermissions(baseContext)) {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            supportFragmentManager.commit {
                replace(R.id.frame_content, CameraFragment())
            }
        }

        val endDrillButton = findViewById<Button>(R.id.end_drill)
        // Set a click listener to the button
        endDrillButton.setOnClickListener {
            endDrill()
        }
    }

    fun endDrill() {

        val drillId = intent.getIntExtra("drillId", -1)

        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        // Find the TextViews for shotsTaken and shotsMade
        val shotsTakenTextView = findViewById<TextView>(R.id.shot_taken_counter)
        val shotsMadeTextView = findViewById<TextView>(R.id.shot_made_counter)

        // Parse the text from TextViews to integers
        val shotsTaken = shotsTakenTextView.text.toString().toInt()
        val shotsMade = shotsMadeTextView.text.toString().toInt()
        var percentage = 0f
        if (shotsTaken !=0) {
            percentage = ((shotsMade*100)/shotsTaken).toFloat()
        }

        val drillSession = DrillSession(
            drillId = drillId,
            shotsTaken = shotsTaken,
            shotsMade = shotsMade,
            percentage = percentage,
            date = currentDate
        )

        thread {
            database.getDrillSessionDao().createDrillSession(drillSession)

            val drill = database.getDrillDao().getDrillById(drillId)
            if (drill != null) {
                drill.shotsTaken += shotsTaken
                drill.shotsMade += shotsMade
                var drillPercentage = 0f
                if (drill.shotsTaken !=0) {
                    drillPercentage = ((drill.shotsMade*100)/drill.shotsTaken).toFloat()
                }
                drill.percentage = drillPercentage
                drill.lastDone = currentDate
                database.getDrillDao().updateDrill(drill)

            }

            runOnUiThread{
                // Create an intent to navigate back to MainActivity
                val intent = Intent(this@DrillActivity, MainActivity::class.java)
                // Start MainActivity using the intent
                startActivity(intent)
                // Finish the current activity to remove it from the back stack
                finish()
            }


        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            } else {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, CameraFragment())
                }
            }
        }

    companion object {
        private const val TAG = "HoopStats"
        private const val  FILENAME_FORMAT = "yyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        fun hasPermissions(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

//    private fun requestCameraPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                cameraPermissionCode
//            )
//        } else {
//            // Camera permission already granted, proceed to open the camera
//            startCamera()
//        }
//    }

//    private fun startCamera() {
//        try {
//            // ... your existing camera setup code ...
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//            cameraProviderFuture.addListener({
//                val cameraProvider = cameraProviderFuture.get()
//
//                val preview = Preview.Builder().build()
//                    .also {
//                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
//                    }
//
//                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//
//                try {
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                        this,
//                        cameraSelector,
//                        preview
//                    )
//                } catch (exc: Exception) {
//                    // Handle exception
//                }
//            }, ContextCompat.getMainExecutor(this))
//        } catch (exc: Exception) {
//            print("Error starting camera")
//        }
//
//    }
}
