package ee.ut.cs.powerwise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.PricesDB

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    var priceArray: MutableLiveData<Array<PriceEntity>> = MutableLiveData(arrayOf())

    // loads the last 7 days available + next if available
    fun refresh() {
        val db = PricesDB.getInstance(app)
        val prices = db.getPriceDao().loadAllPrices()
        priceArray.value = prices
    }

    fun addData(vararg priceEntities: PriceEntity){
        val db = PricesDB.getInstance(app)
        db.getPriceDao().addData(*priceEntities)
    }

    fun getInRange(start: Long, end: Long) {
        val db = PricesDB.getInstance(app)
        priceArray.value = db.getPriceDao().loadInRange(start, end)
    }

    fun getForTime(time: Long): Double? {
        val db = PricesDB.getInstance(app)
        return db.getPriceDao().loadPriceForTime(time - time % 3600)?.price
    }

}