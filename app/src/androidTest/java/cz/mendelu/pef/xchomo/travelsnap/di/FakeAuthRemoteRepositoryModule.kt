package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.auth.AuthRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.FakePlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.fake.MockAuthRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
abstract class FakeAuthRemoteRepositoryModule {

    @Binds
    abstract fun bindAuthRemoteRepository(mockImpl: MockAuthRemoteRepository): IAuthRemoteRepository
}