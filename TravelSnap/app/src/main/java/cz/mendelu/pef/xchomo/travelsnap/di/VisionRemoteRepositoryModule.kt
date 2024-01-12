package cz.mendelu.pef.xchomo.travelsnap.di


import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.VisionAPI
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl

import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VisionRemoteRepositoryModule {
    @Provides
    @Singleton
    fun provideVisionRepository(api: VisionAPI): IVisionRemoteRepository
            = VisionRemoteRepositoryImpl(api)

}