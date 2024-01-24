package cz.mendelu.pef.xchomo.travelsnap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.mendelu.pef.xchomo.travelsnap.model.Place


@Database(entities = [Place::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlacesDatabase : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    companion object {
        private var INSTANCE: PlacesDatabase? = null
        fun getDatabase(context: Context): PlacesDatabase {
            if (INSTANCE == null) {
                synchronized(PlacesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PlacesDatabase::class.java, "places_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

    }
}
