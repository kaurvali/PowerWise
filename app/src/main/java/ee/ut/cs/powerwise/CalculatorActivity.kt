package ee.ut.cs.powerwise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class CalculatorActivity : ComponentActivity() {
    lateinit var dataFetcher: DataFetchers
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PowerWiseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(Modifier.fillMaxSize()) {
                        calculator()
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                startActivity(
                                    Intent(
                                        this@CalculatorActivity,
                                        MainActivity::class.java
                                    )
                                )
                            }) {
                                Text(text = "Tagasi")
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun calculator() {
        val hours = remember { mutableStateOf(0) }
        val energyCost = remember { mutableStateOf(0) }
        val bestTimeBeginning = calculateOptimalTime(hours.value)
        val bestTimeEnding = (bestTimeBeginning + hours.value) % 24
        val averagePrice = model.getAveragePrice(
            ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond(),
            ZonedDateTime.now().with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
        )
        Log.i("AVERAGE", "$averagePrice")
        val calculatedPrice = (energyCost.value / 1000) * hours.value * (averagePrice / 1000.0)
        MaterialTheme {
            Column {
                Text(
                    text = "Please choose the usage time of the device",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                TextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    value = hours.value.toString(),
                    onValueChange = {
                        if (it == "") hours.value = 0
                        else hours.value = it.toInt()
                    }
                )
                Text(
                    text = "Please enter the energy consumption in W of the device you'd like to know the price of electricity for:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                TextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    value = energyCost.value.toString(),
                    onValueChange = {
                        if (it == "") energyCost.value = 0
                        else energyCost.value = it.toInt()
                    }
                )
                Text(
                    text = "The most optimal time to use energy is from $bestTimeBeginning:00 to $bestTimeEnding:00",
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "The use of this product will cost you on average $calculatedPrice €",
                    modifier = Modifier.padding(16.dp))

            }
        }
    }

    private fun calculateOptimalTime(value: Int): Int {
        //val startTime: Long = ZonedDateTime.now() .withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
        //val endTime: Long = ZonedDateTime.now().plusHours(24).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
        val startTime: Long = ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
        val endTime: Long = ZonedDateTime.now().with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
        val data = model.getInRange(startTime, endTime)
        if (data != null) {
            return findMinAvg(data.size, value, data)
        }
        return 0
    }

    private fun findMinAvg(n: Int, k: Int, data: Array<PriceEntity>): Int {
        if (n < k) return -1
        var resIndex = 0
        var currSum = 0.0
        for (i in 0 until k) currSum += data[i].price

        var minSum = currSum

        for (i in k until n) {
            currSum += data[i].price - data[i - k].price

            // Update result if needed
            if (currSum < minSum) {
                minSum = currSum
                resIndex = i - k + 1
            }
        }
        return resIndex
    }

}