package ee.ut.cs.powerwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.components.*
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.*
import java.util.TimeZone

class MainActivity : ComponentActivity() {

    private val baseURL = "https://dashboard.elering.ee/api/nps/price"
    lateinit var dataFetcher: DataFetchers
    val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataFetcher = DataFetchers(this.applicationContext, model)
        // TODO - Check whether data is already in db or note before making a request etc
        Ion.getDefault(applicationContext).conscryptMiddleware.enable(false)
        // Add current date data to the db
        dataFetcher.getAtDate(ZonedDateTime.now())

        setContent {
            PowerWiseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO - MAKE DYNAMIC
                    // this is just a demonstrator
                    val startTime: Long = ZonedDateTime.now().with(LocalTime.MIN)
                        .withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
                    val endTime: Long = ZonedDateTime.now().with(LocalTime.MAX)
                        .withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
                    var data = model.getInRange(startTime, endTime)
                    Column(Modifier.fillMaxSize()) {
                        Header()
                        Info()
                        PriceChart(data, true)
                        DateSelector { chosenDate ->
                            dataFetcher.getAtDate(
                                ZonedDateTime.of(
                                    LocalDateTime.of(
                                        chosenDate,
                                        LocalTime.now()
                                    ), ZoneId.of("UTC")
                                )
                            )
                            data = model.getInRange(
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
                        }
                        CurrentPrice(d)
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