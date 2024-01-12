package cz.mendelu.pef.xchomo.travelsnap.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject


class AuthRemoteRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthRemoteRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser



    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            SignInResult(
                data = UserData(userId = result.user?.uid ?: "", username = result.user?.displayName ?: "", profilePictureUrl = result.user?.photoUrl.toString()),
                errorMessage = null
            )
        }catch(e: Exception){
            SignInResult(
                data = null,
                errorMessage = e.message ?: "Sign-in failed"
            )
        }
    }




    override suspend fun getSignedInUser(): UserData? = firebaseAuth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl.toString()
        )
    }




    // Sign up with email and password
    override suspend fun signUp(email: String, password: String): SignInResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return SignInResult(data = null, errorMessage = "User creation failed.")

            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(email).build()
            user.updateProfile(profileUpdates).await()

            user.reload().await()

            SignInResult(
                data = UserData(
                    userId = user.uid,
                    username = user.displayName ?: email,
                    profilePictureUrl = user.photoUrl?.toString() ?: ""
                ),
                errorMessage = null
            )
        } catch (e: Exception) {
            SignInResult(
                data = null,
                errorMessage = e.localizedMessage ?: "Unknown error occurred"
            )
        }
    }


    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e

        }
    }

    override suspend fun logout() {
        try {
            firebaseAuth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
}