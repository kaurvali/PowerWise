package ee.ut.cs.powerwise.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.*
import ee.ut.cs.powerwise.data.PricesDB
import ee.ut.cs.powerwise.data.network.DataFetchers
import ee.ut.cs.powerwise.utils.Utils
import ee.ut.cs.powerwise.widget.ui.PowerWiseWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

/**
 * Fetches new data from API, saves it to database and updates widget state in the background
 */
class WidgetWorker(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    val TAG = this::class.java.name
    val HOUR_SECONDS = 3600

    companion object {
        fun enqueueWork(context: Context) {
            val workName = this::class.java.name
            val workRequest = OneTimeWorkRequestBuilder<WidgetWorker>().build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(workName, ExistingWorkPolicy.KEEP, workRequest)
        }
    }

    override suspend fun doWork(): Result {
        updateWidget(appContext, WidgetState.DataLoading())
        val dataFetchers = DataFetchers(appContext) {
            PricesDB.getInstance(appContext).getPriceDao().addData(*it.toTypedArray())
        }

        return withContext(Dispatchers.IO) {
            dataFetchers.getAtDate(ZonedDateTime.now())

            createWidgetData()
            return@withContext Result.success()
        }
    }

    private suspend fun updateWidget(context: Context, data: WidgetState) {
        GlanceAppWidgetManager(context).getGlanceIds(PowerWiseWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(
                    context = context,
                    glanceId = glanceId,
                    definition = WidgetStateDefinition,
                    updateState = { data })
            }
        PowerWiseWidget().updateAll(context)
    }

    private suspend fun createWidgetData() {
        val db = PricesDB.getInstance(appContext).getPriceDao()

        val currentUnixTime = System.currentTimeMillis() / 1000
        val currentUnixHour = currentUnixTime - currentUnixTime % HOUR_SECONDS

        val currentPrice = db.loadPriceForTime(currentUnixHour)

        val todayUnixStartTime =
            ZonedDateTime.now().with(LocalTime.MIN).withZoneSameInstant(ZoneId.of("UTC"))
                .toEpochSecond()

        Log.i(TAG, "Today's start time $todayUnixStartTime")
        // Loads average price up to current time
        val averagePrice =
            db.loadAveragePrice(todayUnixStartTime, System.currentTimeMillis() / 1000)

        val nextHourPrice1 = db.loadPriceForTime(currentUnixHour + HOUR_SECONDS)
        val nextHourPrice2 = db.loadPriceForTime(currentUnixHour + HOUR_SECONDS * 2)

        updateWidget(
            appContext,
            WidgetState.Data(
                currentPrice = Utils.convertmWhtokWh(currentPrice.price).toString(),
                averagePrice = Utils.convertmWhtokWh(averagePrice.toDouble()).toString(),
                nextHourPrice1 = Utils.convertmWhtokWh(nextHourPrice1.price).toString(),
                nextHourPrice1Hour = getHourNumberFromUnixTime(nextHourPrice1.datetime),
                nextHourPrice2 = Utils.convertmWhtokWh(nextHourPrice2.price).toString(),
                nextHourPrice2Hour = getHourNumberFromUnixTime(nextHourPrice2.datetime)
            )
        )
    }

    private fun getHourNumberFromUnixTime(time: Long): String {
        return ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(time),
            TimeZone.getDefault().toZoneId()
        ).hour.toString()
    }
}

