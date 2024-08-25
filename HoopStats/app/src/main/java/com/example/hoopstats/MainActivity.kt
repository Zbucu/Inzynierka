package com.example.hoopstats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.hoopstats.data.Drill
import com.example.hoopstats.data.HoopStatsDatabase
import com.example.hoopstats.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), DrillsAdapter.OnItemClickListener {
        private lateinit var binding: ActivityMainBinding
    private var drills: List<Drill> = emptyList() // Make drills array a property of MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_drills)

        if (drills.isEmpty()) {
            val database = HoopStatsDatabase.getDatabase(this)
            val drillDao = database.getDrillDao()

            thread {
                // Fetch drills from the database
//                drillDao.deleteAll()
                val fetchedDrills = drillDao.getAllDrills()

                if (fetchedDrills.isEmpty()) {
                    // If there are no drills in the database, add default drills
                    drillDao.createDrill(
                        Drill(
                            name = "Top of the Key",
                            image = R.drawable.top_of_the_key
                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Left Wing",
                            image = R.drawable.left_wing

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Right Wing",
                            image = R.drawable.right_wing

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Left Corner",
                            image = R.drawable.left_corner

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Right Corner",
                            image = R.drawable.right_corner

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Left Elbow",
                            image = R.drawable.left_elbow

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Right Elbow",
                            image = R.drawable.right_elbow

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Left Block",
                            image = R.drawable.left_block

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Right Block",
                            image = R.drawable.right_block

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Under the Basket",
                            image = R.drawable.under_the_basket

                        ),
                    )
                    drillDao.createDrill(
                        Drill(
                            name = "Free Throw",
                            image = R.drawable.free_throw

                        ),
                    )
                    // Add more default drills if needed

                    // Fetch the drills again after adding default drills
                    val updatedDrills = drillDao.getAllDrills()

                    // Update the drills property on the main thread
                    runOnUiThread {
                        drills = updatedDrills

                        // Initialize the adapter and set it to the RecyclerView
                        val drillsAdapter = DrillsAdapter(drills, this@MainActivity)
                        recyclerView.adapter = drillsAdapter
                    }
                } else {
                    // Update the drills property on the main thread
                    runOnUiThread {
                        drills = fetchedDrills

                        // Initialize the adapter and set it to the RecyclerView
                        val drillsAdapter = DrillsAdapter(drills, this@MainActivity)
                        recyclerView.adapter = drillsAdapter
                    }
                }
            }
        } else {
            // If drills are already fetched, set the adapter to the RecyclerView
            val drillsAdapter = DrillsAdapter(drills, this@MainActivity)
            recyclerView.adapter = drillsAdapter
        }


//        drills = arrayOf(
//            Drill("Top of the Key", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Left Wing", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Right Wing", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Left Corner", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Right Corner", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Left Elbow", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Right Elbow", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Left Block", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Right Block", "Never", "0%", R.drawable.ic_launcher_background),
//            Drill("Under the Basket", "Never", "0%", R.drawable.ic_launcher_background)
//        )


    }

    override fun onItemClick(position: Int) {
        // Retrieve the clicked drill from the array
        // Ensure drills are initialized before accessing them
        if (drills?.isNotEmpty() == true) {
            val clickedDrill = drills[position]
            val intent = Intent(this, DrillSelectedActivity::class.java)
            intent.putExtra("drillId", clickedDrill.drillId)
//            intent.putExtra("drillName", clickedDrill.name)
            startActivity(intent)
        } else {
            // Handle the case where drills are not yet initialized
            // Perhaps show a toast message or take appropriate action
        }
    }

//    fun startDrillSelection(view: View) {
////        val intent = Intent(this, DrillChoiceActivity::class.java)
//        val intent = Intent(this, DrillActivity::class.java)
//        startActivity(intent)
//    }
}