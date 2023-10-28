package com.example.assignment1

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.assignment1.databinding.InternationFlightDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class internation_flight_detail : AppCompatActivity() {
    private lateinit var binding: InternationFlightDetailBinding
    private var countryNamesList: List<String> = emptyList()
    private var selectedDeparture: String? = null
    private var selectedDestination: String? = null
    private lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InternationFlightDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        //create the shared data things
        sharedPreferences = getSharedPreferences("flight_details", Context.MODE_PRIVATE)


        //for the switch button to change the button colour
        val swtButton = binding.switchButton

        swtButton.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                binding.departDate.setBackgroundColor(Color.GREEN)
                binding.returnDate.setBackgroundColor(Color.GREEN)
            } else {
                val originalColor = Color.parseColor("#04D4F0")
                binding.departDate.setBackgroundColor(originalColor)
                binding.returnDate.setBackgroundColor(originalColor)
                }}

        //for departure autocompleteTextView
        countryNamesList = InternationalList.readCsvFile(this)
        val dapartureTextView = binding.intDaparture
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, countryNamesList)
        binding.intDaparture.setAdapter(adapter)

        dapartureTextView.setOnItemClickListener { adapterView, view, position, id ->
            selectedDeparture = adapterView.getItemAtPosition(position) as String
        }


        //for destination autocompleteTextView
        val destinationTextView = binding.intDestination
        binding.intDestination.setAdapter(adapter)

        destinationTextView.setOnItemClickListener { adapterView, view, position, id ->
            selectedDestination = adapterView.getItemAtPosition(position) as String
        }

        binding.departDate.setOnClickListener {
            showDatePickerDialog(it, "DEPARTURE")
        }

        binding.returnDate.setOnClickListener {
            showDatePickerDialog(it, "RETURN")
        }

        //next to loading screen
        val nextButton = binding.btnNext

        val favoriteCheckBox = binding.ckFavorite
        favoriteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                // The CheckBox is checked, provide a frequent flyer discount
                editor.putBoolean("isFrequentFlyer", true)
            } else {
                // The CheckBox is unchecked, remove the frequent flyer discount
                editor.putBoolean("isFrequentFlyer", false)
            }
            editor.apply()
        }
        //click next button
        nextButton.setOnClickListener {
            if(selectedDeparture != null && selectedDestination != null){
                //save the data to sharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("selectedDeparture", selectedDeparture)
                editor.putString("selectedDestination", selectedDestination)
                editor.putBoolean("isInternational", true)
                editor.putBoolean("isDomestic", false)
                editor.apply()


                //start activity loading screen
                val intent = Intent(this,GuestActivity::class.java)
                startActivity(intent)
            }


        }

        //For calender
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,4)

        //show the current date to the date button
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val futureDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

        // Set the current date to the TextView
        binding.departDate.text = currentDate
        binding.returnDate.text = futureDate


    }

    //set the seasoning
    fun isOffSeasoning(month:Int):Boolean{
        return month in 7..10
    }


    //for the date picker
    fun showDatePickerDialog(view: View, dateType: String){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth-${selectedMonth + 1}-$selectedYear"

                //Decide which button to update based on dateType
                val dateButton = if(dateType == "DEPARTURE"){
                    binding.departDate
                } else {
                    binding.returnDate
                }

                dateButton.text = selectedDate

                //save the selected date into shared
                val editor = sharedPreferences.edit()
                editor.putString("$dateType-Date", selectedDate)
                editor.apply()

                if (isOffSeasoning(selectedMonth)){
                    binding.txtSeason.text = "It's off-season month! 15% discount given!"
                    editor.putBoolean("isOffSeason", true)
                }else{
                    binding.txtSeason.text = "It's seasoning month."
                    editor.putBoolean("isOffSeason", false)
                }
                // Check for early reservation
                if (isEarlyReservation(selectedDate)) {
                    binding.earlyReservation.text = "Early reservation! 10% discount given!"  // Make sure to add this TextView in your layout
                    editor.putBoolean("isEarlyReservation", true)
                } else {
                    binding.earlyReservation.text = "Not an early reservation."
                    editor.putBoolean("isEarlyReservation", false)
                }
                editor.apply()
            },
            year,
            month,
            day
        )
        dpd.show()
    }
    // Add this function in your class
    fun isEarlyReservation(departureDateString: String): Boolean {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val departureDate = sdf.parse(departureDateString) ?: return false

        val currentCalendar = Calendar.getInstance()
        val departureCalendar = Calendar.getInstance()

        departureCalendar.time = departureDate

        currentCalendar.add(Calendar.MONTH, 5)

        if (departureCalendar.after(currentCalendar)) {
            return true
        } else {
            return false
        }
    }

}