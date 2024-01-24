package cz.mendelu.pef.xchomo.travelsnap.database


import cz.mendelu.pef.xchomo.travelsnap.model.Place
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(private val dao: PlacesDao)
    : IPlacesRepository {

    override fun getAll(): Flow<List<Place>> {
        return dao.getAll()
    }

    override suspend fun insert(place: Place): Long {
        return dao.insert(place)
    }


    override suspend fun getPlaceById(id: Long): Place =
        dao.getPlaceById(id)

    override suspend fun update(place: Place) {
        dao.update(place)
    }

    override suspend fun getPlacesForDate(date: String): List<Place> {
        return dao.getPlaceByDate(date)
    }

    override suspend fun delete(place: Long) {
        dao.delete(place)
    }
}
