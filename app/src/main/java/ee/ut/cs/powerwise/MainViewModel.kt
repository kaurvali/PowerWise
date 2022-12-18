package ee.ut.cs.powerwise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.PricesDB

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    var priceArray: Array<PriceEntity> = arrayOf()

    // loads the last 7 days available + next if available
    fun refresh() {
        val db = PricesDB.getInstance(app)
        val prices = db.getPriceDao().loadAllPrices()
        priceArray = prices
    }

    // addsData
    fun addData(priceEntity: PriceEntity){
        val db = PricesDB.getInstance(app)
        db.getPriceDao().addData(priceEntity)
        refresh()
    }

    // returns data on a certain date or dates
    fun getInRange(start: Long, end: Long): Array<PriceEntity> {
        val db = PricesDB.getInstance(app)
        return db.getPriceDao().loadInRange(start, end)
    }

    fun getForTime(time: Long): PriceEntity {
        val db = PricesDB.getInstance(app)
        return db.getPriceDao().loadPriceForTime(time - time % HOUR_SECONDS)
    }

}