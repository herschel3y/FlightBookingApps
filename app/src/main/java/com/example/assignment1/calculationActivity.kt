package com.example.assignment1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignment1.databinding.ActivityCalculationBinding
import kotlin.math.min

class calculationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCalculationBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("flight_details", Context.MODE_PRIVATE)
        val departure = sharedPreferences.getString("selectedDeparture", "")
        val destination = sharedPreferences.getString("selectedDestination", "")
        val departureDate = sharedPreferences.getString("DEPARTURE-Date", "Not Set")
        val returnDate = sharedPreferences.getString("RETURN-Date", "Not Set")
        val adultCount = sharedPreferences.getInt("adults", 0)
        val youthCount = sharedPreferences.getInt("youth", 0)
        val infantCount = sharedPreferences.getInt("infant", 0)
        val passengerString = getPassengerString(adultCount, youthCount, infantCount)
        val isInternational = sharedPreferences.getBoolean("isInternational", false)
        val isDomestic = sharedPreferences.getBoolean("isDomestic", false)
        val luggage = sharedPreferences.getString("SelectedLuggage","")
        val season = sharedPreferences.getBoolean("isOffSeason", false)
        val isFrequentFlyer = sharedPreferences.getBoolean("isFrequentFlyer", false)
        val isEarlyReservation = sharedPreferences.getBoolean("isEarlyReservation", false)
        val baseFare = 1000.0
        val originalPrice = baseFare * (adultCount + youthCount + infantCount)
        val finalFare = calculateTotalFare(baseFare, isInternational, season, isFrequentFlyer, isEarlyReservation, adultCount, youthCount, infantCount)
        val btnConfirm = binding.btnConfirm

        binding.intDaparture.text = departure
        binding.intDestination.text = destination
        binding.intDapartDate.text = departureDate
        binding.intReturnDate.text = returnDate
        binding.passenger.text = passengerString
        when {
            isInternational == true -> binding.flightType.text = "International flight"
            isDomestic == true -> binding.flightType.text = "Domestic flight"
            else -> binding.flightType.text = "Unknown flight type"
        }
//        binding.flightType.text = if(isInternational==true) {"International flight"} if(isDomestic==true){ "Domestic flight"}
        binding.luggage.text = luggage
        binding.season.text = if(season) "Off-Seasoning 15% Discount" else "Seasoning"
        binding.bookEarly.text = if(isEarlyReservation) "Eligible for early booking discount" else "No eligible for early booking discount"
        binding.frequentFlyer.text= if(isFrequentFlyer) "Frequently Fly! \n15% discount" else "No Frequently Fly"
        binding.originalPrice.text = originalPrice.toString()
        binding.priceAfterDis.text = finalFare.toString()

        btnConfirm.setOnClickListener {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        }


    }
    fun getPassengerString(adults: Int, youths: Int, infants: Int): String {
        val parts = mutableListOf<String>()

        if (adults > 0) {
            parts.add("$adults ${if (adults > 1) "adults " else "adult"}")
        }

        if (youths > 0) {
            parts.add("$youths ${if (youths > 1) "youths" else "youth"}")
        }

        if (infants > 0) {
            parts.add("$infants ${if (infants > 1) "infants" else "infant"}")
        }

        return parts.joinToString(", ")
    }
    private fun calculateTotalFare(baseFare: Double,
                                   isInternational: Boolean,
                                   isOffSeason: Boolean,
                                   isFrequentFlyer: Boolean,
                                   isEarlyReservation: Boolean,
                                   adultCount: Int,
                                   youthCount: Int,
                                   infantCount: Int): Double {
        var totalDiscount = 0.0

        // Infant discount
        val infantDiscount = if (isInternational) 0.7 else 0.8
        val infantDiscountAmount = baseFare * infantDiscount * infantCount

        // Youth discount
        val youthDiscount = 0.1
        val youthDiscountAmount = baseFare * youthDiscount * youthCount

        // Early reservation discount
        if (isEarlyReservation) {
            totalDiscount += 0.1
        }

        // Off-season discount for international flights
        if (isInternational && isOffSeason) {
            totalDiscount += 0.15
        }

        // Frequent flyer discount
        if (isFrequentFlyer) {
            totalDiscount += 0.15
        }

        // Maximum discount
        if (infantCount > 0) {
            totalDiscount = min(0.8, totalDiscount)
        } else {
            totalDiscount = min(0.2, totalDiscount)
        }

        // Calculate total discount amount for adults and youth
        val otherDiscountAmount = baseFare * totalDiscount * (adultCount + youthCount)

        // Calculate the final fare
        val finalFare = (baseFare * (adultCount + youthCount + infantCount)) - (infantDiscountAmount + youthDiscountAmount + otherDiscountAmount)

        return finalFare
    }

}