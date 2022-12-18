package ee.ut.cs.powerwise.data.network

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.MainViewModel
import ee.ut.cs.powerwise.data.PriceEntity
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DataFetchers(
    val context: Context,
    val model: MainViewModel? = null,
    val baseURL: String = "https://dashboard.elering.ee/api/nps/price",
) {
    // This method gets the price for the next day
    fun getNextDay(callback: ((data: List<PriceEntity>) -> Unit)? = null) {

        val currentTime = ZonedDateTime.now()
        // Change the to the midnight of the next day and covert it to UTC
        val startTime =
            currentTime.with(LocalTime.MIN).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))
        // Change the to the 23:59 of the next day and covert it to UTC
        val endTime =
            currentTime.with(LocalTime.MAX).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))

        // call out the API
        makeRequest(startTime.toString(), endTime.toString(), callback)
    }

    // This method gets the price in a certain range
    fun getLastDaysRange(days: Int, callback: ((data: List<PriceEntity>) -> Unit)? = null) {
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
            makeRequest(startTime.toString(), endTime.toString(), callback)
        }
    }

    // This method gets the price on a certain day
    fun getAtDate(date: ZonedDateTime, callback: ((data: List<PriceEntity>) -> Unit)? = null) {
        val startTime: ZonedDateTime =
            date.with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC"))
        val endTime: ZonedDateTime = date.with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC"))

        // call out the API
        makeRequest(startTime.toString(), endTime.toString(), callback)
    }

    private fun makeRequest(startTime: String, endTime: String, callback: ((data: List<PriceEntity>) -> Unit)? = null) {
        Log.i("HTTP TIMES", "$startTime $endTime")
        Ion.with(context)
            .load(baseURL)
            .addQuery("start", startTime)
            .addQuery("end", endTime)
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    Log.e("HTTP Error", "Something went wrong")
                    e.printStackTrace()
                } else {
                    Log.i("HTTP Result", "Success")
                    val reqData = jsonObjectToPriceArray(result["data"] as JsonObject)
                    if (model != null) {
                        model.addData(*reqData.toTypedArray())
                    }
                    if (callback != null) {
                        callback(reqData)
                    }
                    // addToDB(result["data"] as JsonObject)
                }
            }
    }

    /**
     * If ViewModel is defined, then
     */
    private fun addToDB(data: JsonObject) {
        val ee = data["ee"].asJsonArray
        val prices: MutableList<PriceEntity> = ArrayList()
        for (time in ee) {
            prices.add(
                PriceEntity(
                    time.asJsonObject["timestamp"].asLong,
                    time.asJsonObject["price"].asDouble
                )
            )
        }
        if (model != null) {
            for (price in prices) {
                model.addData(price)
            }
        }
    }

    private fun jsonObjectToPriceArray(data: JsonObject): List<PriceEntity> {
        val ee = data["ee"].asJsonArray
        val prices: MutableList<PriceEntity> = ArrayList()
        for (time in ee) {
            prices.add(
                PriceEntity(
                    time.asJsonObject["timestamp"].asLong,
                    time.asJsonObject["price"].asDouble
                )
            )
        }
        return prices
    }
}