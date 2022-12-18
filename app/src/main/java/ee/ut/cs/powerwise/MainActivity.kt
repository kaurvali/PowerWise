package ee.ut.cs.powerwise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.koushikdutta.ion.Ion
import ee.ut.cs.powerwise.components.*
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.ui.theme.PowerWiseTheme
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

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
                    val startTime: Long = ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
                    val endTime: Long = ZonedDateTime.now().with(LocalTime.MAX).withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()
                    val data = model.getInRange(startTime, endTime)
                    Column(Modifier.fillMaxSize()){
                        Header()
                        Info()
                        DateSelector("12.12.2022")
                        PriceChart(data, true)
                        CurrentPrice(25.0)
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
                                Text(text = "Kalkulaator")
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