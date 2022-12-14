package ee.ut.cs.powerwise

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.components.*
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import ee.ut.cs.powerwise.utils.TimeHelpers.Companion.getTodayMinMaxTime
import java.time.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var dataFetcher: DataFetchers
    val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataFetcher = DataFetchers(this.applicationContext, model)
        // TODO - Check whether data is already in db or note before making a request etc
        Ion.getDefault(applicationContext).conscryptMiddleware.enable(false)
        // Add current date data to the db
        val currentZonedTime = getTodayMinMaxTime(LocalTime.MIN)
            .withZoneSameInstant(ZoneId.of("UTC"))
        val startTime: Long = currentZonedTime.toEpochSecond()
        val endTime: Long = getTodayMinMaxTime(LocalTime.MAX).toEpochSecond()

        dataFetcher.getAtDate(currentZonedTime) {
            model.getInRange(startTime, endTime)
        }

        setContent {
            CompositionLocalProvider(LocalFragmentManagerProvider provides supportFragmentManager) {
                PowerWiseTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val data by model.priceArray.observeAsState()
                        val currentDate = remember { mutableStateOf(LocalDate.now()) }
                        val currentPrice =
                            remember { mutableStateOf(model.getForTime(System.currentTimeMillis() / 1000)) }

                        Column(Modifier.fillMaxSize()) {
                            Header()
                            Info()
                            data?.let { PriceChart(it, true) }
                            DateSelector(currentDate.value) { chosenDate ->
                                dataFetcher.getAtDate(
                                    ZonedDateTime.of(
                                        LocalDateTime.of(
                                            chosenDate,
                                            LocalTime.now()
                                        ), ZoneId.of("UTC")
                                    )
                                ) {
                                    model.getInRange(
                                        ZonedDateTime.of(
                                            chosenDate,
                                            LocalTime.MIN,
                                            TimeZone.getDefault().toZoneId()
                                        ).toEpochSecond(),
                                        ZonedDateTime.of(
                                            chosenDate,
                                            LocalTime.MAX,
                                            TimeZone.getDefault().toZoneId()
                                        ).toEpochSecond()
                                    )
                                    currentDate.value = chosenDate
                                    currentPrice.value =
                                        model.getForTime(System.currentTimeMillis() / 1000)
                                }

                            }
                            CurrentPrice(currentPrice.value)
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(onClick = {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            CalculatorActivity::class.java
                                        )
                                    )
                                }) {
                                    Text(text = getString(R.string.PowerWise_calculator))
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        model.refresh()
    }
}