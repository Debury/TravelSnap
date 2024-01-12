package cz.mendelu.pef.xchomo.travelsnap.di

import cz.mendelu.pef.xchomo.travelsnap.api.PlacesAPI
import cz.mendelu.pef.xchomo.travelsnap.api.VisionAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {


    @Provides
    @Singleton
    fun providePlacesAPI(@PlacesApiRetrofit retrofit: Retrofit): PlacesAPI
            = retrofit.create(PlacesAPI::class.java)



    @Provides
    @Singleton
    fun provideVisionApi(@VisionApiRetrofit retrofit: Retrofit): VisionAPI
            = retrofit.create(VisionAPI::class.java)
}
