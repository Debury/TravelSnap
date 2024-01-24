package cz.mendelu.pef.xchomo.travelsnap.database


import cz.mendelu.pef.xchomo.travelsnap.model.Place
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPlacesRepository {
    fun getAll(): Flow<List<Place>>
    suspend fun insert(place: Place): Long
    suspend fun getPlaceById(id: Long): Place
    suspend fun update(place: Place)

    suspend fun getPlacesForDate(date: String): List<Place>

    suspend fun delete(place: Long)

}
