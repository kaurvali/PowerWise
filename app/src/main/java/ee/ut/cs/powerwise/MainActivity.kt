package ee.ut.cs.powerwise

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.PricesDB
import ee.ut.cs.powerwise.databinding.ActivityMainBinding
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {

    private val baseURL = "https://dashboard.elering.ee/api/nps/price"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO - Check whether data is already in db or note before making a request
        getNextDay()
    }

    // This method gets the price for the next day
    fun getNextDay() {

        val currentTime = ZonedDateTime.now()
        // Change the to the midnight of the next day and covert it to UTC
        val startTime =
            currentTime.with(LocalTime.MIN).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))
        // Change the to the 23:59 of the next day and covert it to UTC
        val endTime =
            currentTime.with(LocalTime.MAX).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))

        // call out the API
        makeRequest(startTime.toString(), endTime.toString())
    }

    // This method gets the price in a certain range
    fun getLastDaysRange(days: Int) {
        // the request can't be more than 365 days
        if (days <= 365) {

            val currentTime = ZonedDateTime.now()
            // get the earliest possible time days back and covert it to UTC
            val startTime: ZonedDateTime = currentTime.with(LocalTime.MIN).minusDays(days.toLong())
                .withZoneSameInstant(ZoneId.of("UTC"))

            // Change the to the 23:59 of the current day and covert it to UTC
            val endTime: ZonedDateTime =
                currentTime.with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC"))

            // call out the API
            makeRequest(startTime.toString(), endTime.toString())
        }
    }

    // This method gets the price on a certain day
    fun getAtDate(date: ZonedDateTime) {

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
                    Log.i("HTTP Result", "Success")
                    addToDB(result["data"] as JsonObject)
                }
            }
    }

    private fun addToDB(data: JsonObject) {
        val dao = PricesDB.getInstance(this).getPriceDao()
        val ee = data["ee"].asJsonArray
        for (time in ee) {
            val price = PriceEntity(
                time.asJsonObject["timestamp"].asLong,
                time.asJsonObject["price"].asDouble
            )
            // add all of the data to db
            dao.addData(price)
        }
        val prices = dao.loadAllPrices()
        for (price in prices) {
            Log.i("DB_Data", price.price.toString() + " " + price.datetime.toString())
        }
    }
}