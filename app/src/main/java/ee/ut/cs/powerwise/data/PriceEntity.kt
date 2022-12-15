package ee.ut.cs.powerwise.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prices")
data class PriceEntity (
    @PrimaryKey
    @ColumnInfo(name = "datetime")
    var datetime: Long,
    @ColumnInfo(name = "price")
    var price: Double
)