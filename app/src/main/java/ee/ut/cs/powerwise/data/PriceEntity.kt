package ee.ut.cs.powerwise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prices")
data class PriceEntity (
    @PrimaryKey
    var datetime: Long,
    var price: Double
)