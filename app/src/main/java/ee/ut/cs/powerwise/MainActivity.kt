package ee.ut.cs.powerwise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.PricesDB
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : ComponentActivity() {

    private val baseURL = "https://dashboard.elering.ee/api/nps/price"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PowerWiseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
                PriceChart()
            }
        }

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
    }

    data class Bar(val value: Float, val color: Color)
}

fun Float.mapValueToDifferentRange(
    inMin: Float,
    inMax: Float,
    outMin: Float,
    outMax: Float
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

@Composable
fun PriceChart(modifier: Modifier = Modifier) {
    // our values to draw
    val bars = listOf(
        MainActivity.Bar(10f, Color.Cyan),
        MainActivity.Bar(20f, Color.Cyan),
        MainActivity.Bar(30f, Color.Cyan),
        MainActivity.Bar(40f, Color.Cyan),
        MainActivity.Bar(10f, Color.Cyan)
    )
    val maxValue = bars.maxOf { it.value } // find max value

    // create Box with canvas
    Box(
        modifier = modifier
            .drawBehind { // we use drawBehind() method to create canvas

                bars.forEachIndexed { index, bar ->
                    // calculate left and top coordinates in pixels
                    val left = index
                        .toFloat()
                        .mapValueToDifferentRange(
                            inMin = 0f,
                            inMax = bars.size.toFloat(),
                            outMin = 0f,
                            outMax = size.width
                        )
                    val top = bar.value
                        .mapValueToDifferentRange(
                            inMin = 0f,
                            inMax = maxValue,
                            outMin = size.height,
                            outMax = 0f
                        )

                    // draw the bars
                    drawRect(
                        color = bar.color,
                        topLeft = Offset(left, top),
                        size = Size(50f, size.height - top)
                    )
                }
            })
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PowerWiseTheme {
        Greeting("Android")
    }
}