package ee.ut.cs.powerwise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class CalculatorActivity : ComponentActivity() {
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
                        Calculator()
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
                                Text(text = getString(R.string.Calculator_back_text))
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Calculator() {
        val hours = remember { mutableStateOf(0) }
        val energyCost = remember { mutableStateOf(0.0) }
        val calculatedPrice = remember { mutableStateOf(0.0) }
        val bestTimeBeginning = calculateOptimalTime(hours.value)
        val bestTimeEnding = (bestTimeBeginning + hours.value) % 24
        // Price is euro/mWh in database
        val averagePrice = model.getAveragePrice(
            ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC"))
                .toEpochSecond(),
            ZonedDateTime.now().with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC"))
                .toEpochSecond()
        )
        Log.i("AVERAGE", "$averagePrice")
        calculatedPrice.value = (energyCost.value / 1000000.0) * hours.value * (averagePrice)
        MaterialTheme {
            Column {
                Text(
                    text = stringResource(R.string.Calculator_help_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                TextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    value = hours.value.toString(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        try {
                            if (it == "") hours.value = 0
                            else hours.value = it.toInt()
                        } catch (_: java.lang.NumberFormatException) {
                        }
                    }
                )
                Text(
                    text = stringResource(R.string.Calculator_energy_consumption_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                TextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    value = energyCost.value.toString(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        try {
                            if (it == "") energyCost.value = 0.0
                            else energyCost.value = it.toDouble()
                        } catch (_: NumberFormatException) {
                        }
                    }
                )
                Text(
                    text = stringResource(
                        R.string.Calculator_optimal_time_text,
                        bestTimeBeginning,
                        bestTimeEnding
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = stringResource(
                        R.string.Calculator_average_price_text,
                        calculatedPrice.value
                    ),
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    }

    private fun calculateOptimalTime(value: Int): Int {
        val startTime: Long =
            ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC"))
                .toEpochSecond()
        val endTime: Long =
            ZonedDateTime.now().with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC"))
                .toEpochSecond()
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