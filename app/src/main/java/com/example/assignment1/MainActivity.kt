package com.example.assignment1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignment1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        binding.btnDomestic.setOnClickListener {
            val intent = Intent(this, domestic_flight_detail::class.java)
            startActivity(intent)
        }
        binding.btnInternational.setOnClickListener {
            val intent = Intent(this, internation_flight_detail::class.java)
            startActivity(intent)
        }

    }
}