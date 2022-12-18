package ee.ut.cs.powerwise.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ee.ut.cs.powerwise.data.PriceEntity
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializes/deserializes widget state information
 */
object WidgetStateDefinition: GlanceStateDefinition<WidgetState> {
    private const val DATA_STORE_FILENAME = "PowerWiseWidgetState"
    private val gson = Gson()

    private val Context.datastore by dataStore(DATA_STORE_FILENAME, WidgetStateSerializer)

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<WidgetState> {
        return context.datastore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILENAME)
    }

    object WidgetStateSerializer: Serializer<WidgetState> {
        override val defaultValue: WidgetState
            get() = WidgetState.DataLoadingFailed("Data unavailable!")

        override suspend fun readFrom(input: InputStream): WidgetState {
            return try {
                val data = gson.fromJson(input.readBytes().decodeToString(),WidgetState.Data::class.java)
                data ?: WidgetState.DataLoadingFailed("Data loading failed!")
            } catch (error: JsonSyntaxException) {
                WidgetState.DataLoadingFailed("Data loading failed!")
            }

        }

        override suspend fun writeTo(t: WidgetState, output: OutputStream) {
           output.use {
                it.write(gson.toJson(t).encodeToByteArray())
           }
        }

    }


}