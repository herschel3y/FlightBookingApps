import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

object InternationalList {
    fun readCsvFile(context: Context): List<String> {
        val countryList = mutableListOf<String>()

        try {
            val inputStream = context.assets.open("list_of_countries.csv")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.readLine()  // Skip header line

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",")
                if (tokens.size >= 2) {
                    val country = tokens[1].trim()
                    countryList.add(country.toLowerCase(Locale.getDefault()))

                }
            }

            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return countryList
    }
}
