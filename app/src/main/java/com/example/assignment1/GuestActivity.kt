package com.example.assignment1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignment1.databinding.ActivityGuestBinding


class GuestActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityGuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        //create the shared data things
        sharedPreferences = getSharedPreferences("flight_details", Context.MODE_PRIVATE)

        val radioGroup = binding.stateLug
        val btnSearch = binding.btnSearch

        btnSearch.setOnClickListener {
            val intent = Intent(this, loadingScreen::class.java)

            val selectedLuggage: String = when (radioGroup.checkedRadioButtonId) {
                R.id.handLug -> "Hand Luggage"
                R.id.checkLug -> "Checked Luggage"
                else -> "Unknown"
            }
            val adultCount = binding.adults.text.toString().toInt()
            val youthCount = binding.youth.text.toString().toInt()
            val infantCount = binding.infant.text.toString().toInt()

            val editor = sharedPreferences.edit()
            editor.putString("SelectedLuggage", selectedLuggage)
            editor.putInt("adults", adultCount)
            editor.putInt("youth", youthCount)
            editor.putInt("infant", infantCount)
            editor.apply()

            startActivity(intent)

        }

    }
}