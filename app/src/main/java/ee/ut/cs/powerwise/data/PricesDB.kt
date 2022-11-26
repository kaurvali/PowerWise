package ee.ut.cs.powerwise.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ PriceEntity::class ], version = 3)
abstract class PricesDB : RoomDatabase() {
    abstract fun getPriceDao(): PriceDAO

    companion object {
        private lateinit var db : PricesDB

        @Synchronized fun getInstance(context: Context) : PricesDB {

            if (!this::db.isInitialized) {
                db = Room.databaseBuilder(
                    context, PricesDB::class.java, "prices")
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return db

        }
    }
}