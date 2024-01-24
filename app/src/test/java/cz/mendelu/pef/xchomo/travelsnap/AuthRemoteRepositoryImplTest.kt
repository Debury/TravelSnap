package cz.mendelu.pef.xchomo.travelsnap


import android.text.TextUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import cz.mendelu.pef.xchomo.travelsnap.auth.AuthRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.concurrent.Task
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class AuthRemoteRepositoryImplTest {

    private lateinit var authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
    private lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setup() {



        mockFirebaseUser = mockk()
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockFirebaseAuth = FirebaseAuth.getInstance()
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        authRemoteRepositoryImpl = AuthRemoteRepositoryImpl(mockFirebaseAuth)


    }

    @Test
    fun signInWithEmailAndPassword_Success() = runBlocking {
        val email = "test@example.com"
        val password = "password"

        val mockAuthResult = mockk<AuthResult>()

        every { mockFirebaseAuth.signInWithEmailAndPassword(email, password) } coAnswers {
            mockk {
                coEvery { isSuccessful } returns true
                every { result } returns mockAuthResult
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
            }
        }

        // Mock properties of mockFirebaseUser
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        every { mockAuthResult.user } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns "userId123"
        every { mockFirebaseUser.displayName } returns "John Doe"
        every { mockFirebaseUser.photoUrl } returns null

        val result = authRemoteRepositoryImpl.signInWithEmailAndPassword(email, password)
        val expectedUserData = UserData("userId123", "John Doe", null)
        val expectedSignInResult = SignInResult(expectedUserData, null)

        assertEquals(expectedSignInResult.data?.userId, result.data?.userId)
    }

    @Test
    fun signInWithEmailAndPassword_Failure() = runBlocking {

        val email = "john.doe@example.com"
        val password = "password123"

        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        val mockAuthResult = mockk<AuthResult>()

        every { mockFirebaseAuth.signInWithEmailAndPassword(email, password) } coAnswers {
            mockk {
                coEvery { isSuccessful } returns false
                every { result } returns null
                every { isComplete } returns true
                every { exception } returns Exception("Sign-in failed")
                every { isCanceled } returns false
            }
        }
        val result = authRemoteRepositoryImpl.signInWithEmailAndPassword(email, password)
        val expectedSignInResult = SignInResult(null, "Sign-in failed")
        assertEquals(expectedSignInResult.errorMessage, result.errorMessage)
    }

    @Test
    fun getSignedInUser_Success() {

        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns "userId123"
        every { mockFirebaseUser.displayName } returns "John Doe"
        every { mockFirebaseUser.photoUrl.toString() } returns "https://example.com/photo.jpg"

        val result = authRemoteRepositoryImpl.currentUser

        val expectedUserData = UserData("userId123", "John Doe", "https://example.com/photo.jpg")
        assertEquals(expectedUserData.userId, (result as FirebaseUser).uid)
    }

    @Test
    fun getSignedInUser_NullUser() {
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.currentUser } returns null

        val result = authRemoteRepositoryImpl.currentUser

        assertEquals(null, result)
    }


    @Test
    fun signUp_Success() = runBlocking {
        val email = "test@example.com"
        val password = "password"

        val mockAuthResult = mockk<AuthResult>()
        // Mocking FirebaseUser
        val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
        every { mockFirebaseUser.uid } returns "userId456"
        every { mockFirebaseUser.displayName } returns email
        every { mockFirebaseUser.photoUrl } returns null
        every { mockAuthResult.user } returns mockFirebaseUser

        val mockUserProfileChangeRequest = mockk<UserProfileChangeRequest>()
        val mockUserProfileChangeRequestBuilder = mockk<UserProfileChangeRequest.Builder>(relaxed = true)
        every { mockUserProfileChangeRequestBuilder.setDisplayName(email) } returns mockUserProfileChangeRequestBuilder
        every { mockUserProfileChangeRequestBuilder.build() } returns mockUserProfileChangeRequest


        coEvery { mockFirebaseAuth.createUserWithEmailAndPassword(email, password) } coAnswers {
            mockk {
                coEvery { isSuccessful } returns true
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
                every { result } coAnswers {
                    mockk {
                        coEvery { user } returns mockFirebaseUser
                    }
                }
            }
        }


        every { mockFirebaseUser.updateProfile(mockUserProfileChangeRequest) } coAnswers {
            mockk {
                coEvery { isSuccessful } returns true
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
            }
        }

        every { mockFirebaseUser.reload() } coAnswers {
            mockk {
                coEvery { isSuccessful } returns true
                every { isComplete } returns true
                every { exception } returns null
                every { isCanceled } returns false
            }
        }





        val result = authRemoteRepositoryImpl.signUp(email, password)


        val expectedUserData = UserData("userId456", email, "")
        val expectedSignInResult = SignInResult(expectedUserData, null)

        assertEquals(expectedSignInResult, result)
    }

    @Test
    fun signUp_Failure() = runBlocking {
        val email = "jane.doe@example.com"
        val password = "password123"

        val mockAuthResult = mockk<AuthResult>()
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseUser.uid } returns "userId789"
        every { mockFirebaseUser.displayName } returns email
        every { mockFirebaseUser.photoUrl } returns null

        every { mockAuthResult.user } returns mockFirebaseUser
        every { mockFirebaseAuth.createUserWithEmailAndPassword(email, password) } coAnswers {
            mockk {
                coEvery { isSuccessful } returns true
                every { result } coAnswers {
                    mockk {
                        coEvery { user } returns mockFirebaseUser
                    }
                }
            }
        }
        val result = authRemoteRepositoryImpl.signUp(email, password)
        val expectedSignInResult = SignInResult(null, "Sign-up failed")
        assertEquals(expectedSignInResult.data, result.data)

    }
    @Test
    fun signOut_Success() = runBlocking {
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.signOut() } returns Unit
        authRemoteRepositoryImpl.signOut()
        verify { mockFirebaseAuth.signOut() }
    }

    @Test
    fun signOut_Failure() = runBlocking {
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.signOut() } throws Exception("Sign-out failed")

        try {
            authRemoteRepositoryImpl.signOut()
        } catch (e: Exception) {
            assertEquals("Sign-out failed", e.message)
        }
    }

}
