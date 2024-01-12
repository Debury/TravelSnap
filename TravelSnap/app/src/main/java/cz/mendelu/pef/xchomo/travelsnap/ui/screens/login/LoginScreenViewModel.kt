package cz.mendelu.pef.xchomo.travelsnap.ui.screens.login

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.auth.AuthRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class LoginScreenViewModel @Inject constructor(
    val authRemoteRepositoryImpl: IAuthRemoteRepository,
) : BaseViewModel(), LoginScreenActions {

    open val authUIState: MutableState<UIState<UserData, LoginErrors>> = mutableStateOf(UIState())

    val currentUser: FirebaseUser?
        get() = authRemoteRepositoryImpl.currentUser

    init {
        if (currentUser != null) {
            authUIState.value = UIState(
                data = UserData(
                    currentUser!!.displayName ?: "",
                    currentUser!!.email ?: "",
                    currentUser!!.photoUrl.toString()
                )
            )
        }
    }

    fun onSignInResult(result: SignInResult) {
        authUIState.value = UIState(loading = true, data = result.data, errors = null)
    }

    fun resetUIState() {
        authUIState.value = UIState()
    }




    override fun signInWithEmailAndPassword(email: String, password: String) {
        authUIState.value.loading = true
        launch {
            val authResult = withContext(Dispatchers.IO) {
                authRemoteRepositoryImpl.signInWithEmailAndPassword(email, password)
            }
            try {
                if (authResult.data != null) {
                    authUIState.value =
                        UIState(loading = false, data = authResult.data, errors = null)
                } else {
                    authUIState.value = UIState(
                        loading = false,
                        data = null,
                        errors = LoginErrors(usernameError = authResult.errorMessage)
                    )
                }
            } catch (e: Exception) {
                authUIState.value =
                    UIState(loading = false, errors = LoginErrors(communicationError = "Check your network connection"))
            }
        }
    }


    override fun signOut() {
        launch {
            authRemoteRepositoryImpl.signOut()
        }
    }

    override fun signUp(email: String, password: String) {
        authUIState.value.loading = true
        launch {
            val authResult = withContext(Dispatchers.IO) {
                authRemoteRepositoryImpl.signUp(email, password)
            }

            try {
                if (authResult.data != null) {
                    authUIState.value =
                        UIState(loading = false, data = authResult.data, errors = null)
                } else {
                    authUIState.value = UIState(
                        loading = false,
                        data = null,
                        errors = LoginErrors(usernameError = authResult.errorMessage)
                    )
                }
            } catch (e: Exception) {
                authUIState.value =
                    UIState(loading = false, errors = LoginErrors(communicationError = authResult.errorMessage))
            }
        }
    }

    fun clearError() {
        authUIState.value = UIState(
            loading = false,
            data = null,
            errors = null
        )
    }


}