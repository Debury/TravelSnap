package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDao
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDatabase(database: PlacesDatabase): PlacesDao
            = database.placesDao()
}