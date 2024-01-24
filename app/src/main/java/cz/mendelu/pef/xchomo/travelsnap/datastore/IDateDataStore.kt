package cz.mendelu.pef.xchomo.travelsnap.datastore

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IDateDataStore {
    suspend fun saveSelectedDate(date: LocalDate)
    fun getSelectedDate(): Flow<LocalDate?>
}