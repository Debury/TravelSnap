package cz.mendelu.pef.xchomo.travelsnap.di


import android.content.Context
import cz.mendelu.pef.xchomo.travelsnap.TravelSnapApplication
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): PlacesDatabase
            = PlacesDatabase.getDatabase(appContext)
}