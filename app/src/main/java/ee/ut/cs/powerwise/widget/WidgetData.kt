package ee.ut.cs.powerwise.widget

/**
 * Helper classes to hold and indicate widget state (loading, loading failed, data)
 */
interface WidgetState {
    data class DataLoading(val message: String = ""): WidgetState

    data class Data(
        val currentPrice: String,
        val averagePrice: String,
        val nextHourPrice1: String,
        val nextHourPrice1Hour: String,
        val nextHourPrice2: String,
        val nextHourPrice2Hour: String,
    ): WidgetState

    data class DataLoadingFailed(val message: String = "Loading failed"): WidgetState
}



