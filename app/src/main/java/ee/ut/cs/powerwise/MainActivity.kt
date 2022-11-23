package ee.ut.cs.powerwise

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.koushikdutta.ion.Ion
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private val baseURL = "https://dashboard.elering.ee/api/nps/price"
    private val timeFormat = "HH:mm"
    private val dateFormat = "dd.MM.yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getNextDay()
        Log.i("TimeToString", getDateToString(1669233886000))
        Log.i("TimeToString", getTimeToString(1669233886000))
    }

    // This method gets the price for the next day
    private fun getNextDay() {

        val currentTime = ZonedDateTime.now()
        // Change the to the midnight of the next day and covert it to UTC
        val startTime = currentTime.with(LocalTime.MIDNIGHT).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))
        // Change the to the 23:59 of the next day and covert it to UTC
        val endTime = currentTime.with(LocalTime.MAX).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))

        // call out the API
        makeRequest(startTime.toString(), endTime.toString())
    }

    // This method gets the price in a certain range
    private fun getLastDaysRange(days: Int) {
        // the request can't be more than 365 days
        if (days<=365) {

            val currentTime = ZonedDateTime.now()
            // get the earliest possible time days back and covert it to UTC
            val startTime: ZonedDateTime = currentTime.with(LocalTime.MIN).minusDays(days.toLong()).withZoneSameInstant(ZoneId.of("UTC"))

            // Change the to the 23:59 of the current day and covert it to UTC
            val endTime: ZonedDateTime = currentTime.with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC"))

            // call out the API
            makeRequest(startTime.toString(), endTime.toString())
        }
    }

    // This method gets the price on a certain day
    private fun getAtDate(date: ZonedDateTime) {

        val startTime: ZonedDateTime = date.with(LocalTime.MIN)
        val endTime: ZonedDateTime = date.with(LocalTime.MAX)

        // call out the API
        makeRequest(startTime.toString(), endTime.toString())
    }

    private fun makeRequest(startTime: String, endTime: String) {
        Log.i("HTTP TIMES", "$startTime $endTime")
        Ion.with(this)
            .load(baseURL)
            .addQuery("start", startTime)
            .addQuery("end", endTime)
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    Log.e("HTTP Error", "Something went wrong")
                } else {
                    Log.i("HTTP Result", result.toString())
                    TODO("Do something with the results...")
                }
            }
    }

    // Unix Milliseconds to date in string
    private fun getTimeToString(time: Long): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat)
        return formatter.format(ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneId.of("UTC")))

    }
    
    // Unix Milliseconds to date in string
    private fun getDateToString(time: Long): String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat)
        return formatter.format(ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneId.of("UTC")))
    }
}