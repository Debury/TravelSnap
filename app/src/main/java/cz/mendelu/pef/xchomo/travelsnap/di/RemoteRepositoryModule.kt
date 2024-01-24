package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.PlacesAPI
import cz.mendelu.pef.xchomo.travelsnap.api.PlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.api.VisionAPI
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDao
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {


    @Provides
    @Singleton
    fun provideMockRemoteRepository(mockAPI: PlacesAPI): IPlacesRemoteRepository
            = PlacesRemoteRepositoryImpl(mockAPI)


}