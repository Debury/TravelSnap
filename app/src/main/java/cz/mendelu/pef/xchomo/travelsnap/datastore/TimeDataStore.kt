package cz.mendelu.pef.xchomo.travelsnap.datastore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

object TimeDataStore {
    private val Context.dataStore by preferencesDataStore(name = "time_preferences")

    private val SELECTED_TIME_KEY = stringPreferencesKey("selected_time_key")

    suspend fun saveSelectedTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_TIME_KEY] = time.toString()
        }
    }
    fun getSelectedTime(context: Context): Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_TIME_KEY]?.toLongOrNull()
        }
}
