package ee.ut.cs.powerwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.components.*
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.*
import java.util.*

class MainActivity : ComponentActivity() {

    lateinit var dataFetcher: DataFetchers
    val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataFetcher = DataFetchers(this.applicationContext, model)
        // TODO - Check whether data is already in db or note before making a request etc
        Ion.getDefault(applicationContext).conscryptMiddleware.enable(false)
        // Add current date data to the db
        dataFetcher.getAtDate(ZonedDateTime.now())

        val currentZonedTime = ZonedDateTime.now().with(LocalTime.MIN)
            .withZoneSameInstant(ZoneId.of("UTC"))
        val startTime: Long = currentZonedTime.toEpochSecond()
        val endTime: Long = ZonedDateTime.now().with(LocalTime.MAX)
            .withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()

        dataFetcher.getAtDate(currentZonedTime) {
            model.getInRange(startTime, endTime)
        }


        setContent {
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