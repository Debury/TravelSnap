package cz.mendelu.pef.xchomo.travelsnap.auth

import android.content.Intent
import android.content.IntentSender
import com.google.firebase.auth.FirebaseUser
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UserData


interface IAuthRemoteRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResult

    suspend fun signOut()

    suspend fun signUp(email: String, password: String): SignInResult



    suspend fun getSignedInUser(): UserData?

    suspend fun logout()
}