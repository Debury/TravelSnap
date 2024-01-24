package cz.mendelu.pef.xchomo.travelsnap.di


import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import cz.mendelu.pef.xchomo.travelsnap.auth.AuthRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRemoteRepository(
    ): IAuthRemoteRepository {
        return AuthRemoteRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
    }
}



