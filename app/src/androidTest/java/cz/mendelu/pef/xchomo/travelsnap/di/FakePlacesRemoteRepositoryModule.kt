package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.fake.FakePlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.VisionAPI
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.fake.FakeVisionRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteRepositoryModule::class]
)
abstract class FakePlacesRemoteRepositoryModule{

    @Binds
    abstract fun providePlacesRemoteRepository(service: FakePlacesRemoteRepositoryImpl): IPlacesRemoteRepository



}