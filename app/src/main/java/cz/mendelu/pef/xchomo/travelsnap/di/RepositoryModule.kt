package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.database.IPlacesRepository
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDao
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RepositoryModule {
    @Provides
    @Singleton
    fun providePlacesRepository(dao: PlacesDao): IPlacesRepository
            = PlacesRepositoryImpl(dao)
}