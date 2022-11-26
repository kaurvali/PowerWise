package ee.ut.cs.powerwise.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class HourlyPrice (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var datetime: ZonedDateTime,
    var price: Double
)