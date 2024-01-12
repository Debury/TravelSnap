package cz.mendelu.pef.xchomo.travelsnap.fake

import android.content.Intent
import android.content.IntentSender
import com.google.firebase.auth.FirebaseUser
import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import javax.inject.Inject

class MockAuthRemoteRepository @Inject constructor() : IAuthRemoteRepository {

    private var mockUser: FirebaseUser? = null

    override val currentUser: FirebaseUser?
        get() = mockUser

    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return SignInResult(
            data = UserData(userId = "mockUserId", username = "mockUsername", profilePictureUrl = "mockUrl"),
            errorMessage = null
        )
    }


    

    override suspend fun getSignedInUser(): UserData? {
        return UserData(
            userId = "mockUserId",
            username = "mockUsername",
            profilePictureUrl = "mockUrl"
        )
    }

    override suspend fun logout() {
        mockUser = null
    }

    override suspend fun signUp(email: String, password: String): SignInResult {
        return SignInResult(
            data = UserData(userId = "mockUserId", username = "mockUsername", profilePictureUrl = "mockUrl"),
            errorMessage = null
        )
    }

    override suspend fun signOut() {
        mockUser = null
    }




}
