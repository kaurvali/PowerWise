package ee.ut.cs.powerwise.utils

import kotlin.math.roundToInt

class Utils {
    companion object {
        fun convertmWhtokWh(price: Double): Double {
            return (price/10*100).roundToInt() / 100.0
        }
    }
}