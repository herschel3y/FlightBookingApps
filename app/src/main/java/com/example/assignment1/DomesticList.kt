package com.example.assignment1

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object DomesticList {
    fun readCsvFile(context: Context): List<String> {
        val countryList = mutableListOf<String>()

        try {
            val inputStream = context.assets.open("malaysia.csv")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.readLine()  // Skip header line

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",")

                val country = tokens[0].trim()
                countryList.add(country.toLowerCase(Locale.getDefault()))


            }

            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return countryList
    }
}