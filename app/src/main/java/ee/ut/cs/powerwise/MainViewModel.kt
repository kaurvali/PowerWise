package ee.ut.cs.powerwise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ee.ut.cs.powerwise.data.PriceEntity
import ee.ut.cs.powerwise.data.PricesDB

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    var priceArray: Array<PriceEntity> = arrayOf()

    fun refresh() {
        val db = PricesDB.getInstance(app)
        val prices = db.getPriceDao().loadAllPrices()
        priceArray = prices
    }

}