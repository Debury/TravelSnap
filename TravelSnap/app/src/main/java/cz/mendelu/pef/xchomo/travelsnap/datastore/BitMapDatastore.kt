package cz.mendelu.pef.xchomo.travelsnap.datastore

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.ExperimentalEncodingApi

object BitmapDataStore {
    private val Context.dataStore by preferencesDataStore(name = "bitmap_preferences")

    private val SELECTED_BITMAP_KEY = stringPreferencesKey("selected_bitmap_key")

    suspend fun saveSelectedBitmap(context: Context, bitmap: Bitmap?) {
        context.dataStore.edit { preferences ->
            if (bitmap != null) {
                preferences[SELECTED_BITMAP_KEY] = bitmap.encodeToBase64()
            }
        }
    }

    fun getSelectedBitmap(context: Context): Flow<Bitmap?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_BITMAP_KEY]?.decodeBase64ToBitmap()
        }

    suspend fun clearSelectedBitmap(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_BITMAP_KEY)
        }
    }
    @OptIn(ExperimentalEncodingApi::class)
    private fun Bitmap.encodeToBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun String?.decodeBase64ToBitmap(): Bitmap? {
        return try {
            if (this != null) {
                val decodedBytes = Base64.decode(this, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}