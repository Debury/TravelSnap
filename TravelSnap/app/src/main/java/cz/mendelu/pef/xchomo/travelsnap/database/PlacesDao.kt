package cz.mendelu.pef.xchomo.travelsnap.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PlacesDao {

    @Query("SELECT * FROM places")
    fun getAll(): Flow<List<Place>>

    @Insert
    suspend fun insert(task: Place): Long



    @Query("SELECT * FROM places WHERE databaseId = :id")
    suspend fun getPlaceById(id: Long): Place

    @Update
    suspend fun update(task: Place)

    @Query("SELECT * FROM places WHERE selectedDate = :date")
    suspend fun getPlaceByDate(date: String): List<Place>

    @Query("DELETE FROM places WHERE databaseId = :id")
    suspend fun delete(id: Long)

}
