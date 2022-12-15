package ee.ut.cs.powerwise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PriceDAO {

    @Query("SELECT * FROM prices")
    fun loadAllPrices(): Array<PriceEntity>

    @Query("SELECT * FROM prices WHERE datetime >= :startTime AND datetime <= :endTime")
    fun loadInRange(startTime: Long, endTime: Long): Array<PriceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(vararg price: PriceEntity)
}