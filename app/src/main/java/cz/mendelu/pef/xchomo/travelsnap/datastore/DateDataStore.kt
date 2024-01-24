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

object DateDataStore {
    private val Context.dataStore by preferencesDataStore(name = "date_preferences")

    private val SELECTED_DATE_KEY = stringPreferencesKey("selected_date_key")

    suspend fun saveSelectedDate(context: Context, date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_DATE_KEY] = date.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSelectedDate(context: Context): Flow<LocalDate?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_DATE_KEY]?.let { LocalDate.parse(it) }
        }
}
