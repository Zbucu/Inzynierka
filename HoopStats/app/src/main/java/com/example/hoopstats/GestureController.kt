package com.example.hoopstats

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.tasks.components.containers.Category

class GestureController(private val context: Context, private val activity: AppCompatActivity) {

    private var gestures: MutableList<String> = mutableListOf()
    private var previousActionTime= 0L
    private val pauseBetweenActions = 1000L

    private var shotTakenCount = 0
    private var shotMadeCount = 0
    var shotTakenCounterTextView = activity.findViewById<TextView>(R.id.shot_taken_counter)
    var shotMadeCounterTextView = activity.findViewById<TextView>(R.id.shot_made_counter)


    fun addGestureCaptured(gestureCategory: List<Category>?) {
        if (System.currentTimeMillis() - previousActionTime < pauseBetweenActions) {
            return
        }

        val gesture: String? = gestureCategory?.getOrNull(0)?.categoryName()?.toString()
        if (gestures.size > 10 || gesture == null) {
            processGestures()
            return
        }

        gestures.add(gesture)
        processGestures()
    }

    private fun processGestures() {
        if (gestures.size <= 10) {
            return
        }

        // Count labels and select the most common one
        val gestureShowed = gestures.maxByOrNull { gesture -> gestures.count { it == gesture } }

        gestures.clear()

        // Don't put function into sleep if detected gesture is None
        if (gestureShowed != "None") {
            previousActionTime = System.currentTimeMillis()
        }

        if (gestureShowed == "Thumb_Down") {
            shotMissed()
        } else if (gestureShowed == "Thumb_Up") {
            shotMade()
        }
    }

    private fun shotMissed(){
        shotTakenCount++
        shotTakenCounterTextView.text = shotTakenCount.toString()
    }

    private fun shotMade(){
        shotTakenCount++
        shotMadeCount++
        shotTakenCounterTextView.text = shotTakenCount.toString()
        shotMadeCounterTextView.text = shotMadeCount.toString()
    }

}