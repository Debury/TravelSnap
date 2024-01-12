package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.FakeLocationManagerImpl
import cz.mendelu.pef.xchomo.travelsnap.location.ILocationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocationModule::class]
)
abstract class FakeLocationManagerModule {

    @Binds
    abstract fun bindAuthRemoteRepository(mockImpl: FakeLocationManagerImpl): ILocationManager
}