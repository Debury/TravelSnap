package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.database.IPlacesRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.FakePlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.fake.FakeVisionRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [VisionRemoteRepositoryModule::class]
)
abstract class FakeVisionRemoteRepositoryModule{

    @Binds
    abstract fun providePlacesRepository(service: FakeVisionRemoteRepositoryImpl): IVisionRemoteRepository



}