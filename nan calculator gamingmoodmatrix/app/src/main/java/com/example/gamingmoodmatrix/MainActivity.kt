package com.example.gamingmoodmatrix

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
// 1. ADD THIS IMPORT LINE AT THE TOP:
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private val selectedMoods = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 2. ADD THIS EXACTLY HERE, BEFORE super.onCreate:
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        // ... the rest of your button logic continues below ...

        // Setup Info Modal Button
        findViewById<Button>(R.id.btnInfo).setOnClickListener {
            showInstructionsModal()
        }

        // Setup Calculator Buttons
        val buttons = listOf(
            R.id.btnAction to "Action",
            R.id.btnStealth to "Stealth",
            R.id.btnOpenWorld to "Open World",
            R.id.btnStory to "Sci-Fi / Story",
            R.id.btnSports to "Sports",
            R.id.btnHardcore to "Hardcore"
        )

        for ((id, mood) in buttons) {
            findViewById<Button>(id).setOnClickListener {
                addMood(mood)
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            selectedMoods.clear()
            updateDisplay()
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateGame()
        }
    }

    private fun addMood(mood: String) {
        if (selectedMoods.size < 2) {
            selectedMoods.add(mood)
            updateDisplay()
        } else {
            tvDisplay.text = "Max 2 moods. Press =\n${selectedMoods.joinToString(" + ")}"
        }
    }

    private fun updateDisplay() {
        if (selectedMoods.isEmpty()) {
            tvDisplay.text = "Select Moods..."
        } else {
            tvDisplay.text = selectedMoods.joinToString(" + ")
        }
    }

    private fun calculateGame() {
        if (selectedMoods.size < 2) {
            tvDisplay.text = "Need 2 moods!\n${selectedMoods.joinToString(" + ")}"
            return
        }

        val combination = selectedMoods.sorted().joinToString(" + ")

        // The "Math" behind the calculator
        val result = when (combination) {
            "Action + Open World" -> "Play: Marvel's Spider-Man 2"
            "Action + Stealth" -> "Play: Ghost of Tsushima"
            "Action + Hardcore" -> "Play: Dead Cells"
            "Sci-Fi / Story + Stealth" -> "Play: Control"
            "Open World + Sports" -> "Play: EA Sports FC 26"
            "Action + Sports" -> "Play: EA Sports FC 26"
            else -> "Play: Astro's Playroom"
        }

        tvDisplay.text = "$combination \n= \n$result"
        selectedMoods.clear() // Reset for next calculation
    }

    private fun showInstructionsModal() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_instructions)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnClose = dialog.findViewById<Button>(R.id.btnCloseModal)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}