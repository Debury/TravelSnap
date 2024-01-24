package cz.mendelu.pef.xchomo.travelsnap.fake

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xchomo.travelsnap.model.SignInResult
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginErrors
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import cz.mendelu.pef.xchomo.travelsnap.R

class MockLoginScreenViewModel: LoginScreenViewModel(
    authRemoteRepositoryImpl =  MockAuthRemoteRepository()

){
    override val authUIState: MutableState<UIState<UserData, LoginErrors>>
            = mutableStateOf(UIState())
    override fun signInWithEmailAndPassword(email: String, password: String) {
        authUIState.value = UIState(loading = true)
        CoroutineScope(Dispatchers.Main).launch {
            val authResult = withContext(Dispatchers.IO) {
                authRemoteRepositoryImpl.signInWithEmailAndPassword(email, password)
            }
            try {
               if (authResult.errorMessage != null) {
                   authUIState.value = UIState(
                       loading = false,
                       errors = LoginErrors(
                           passwordError = authResult.errorMessage
                       )
                   )
               } else {
                   authUIState.value = UIState(
                       loading = false,
                       data = authResult.data,
                       errors = null
                   )
               }
            } catch (e: Exception) {
                authUIState.value = UIState(
                    loading = false,
                    errors = LoginErrors(communicationError = "Communication Error")
                )
            }
        }
    }

    override fun signUp(email: String, password: String) {
        authUIState.value = UIState(loading = true)
        CoroutineScope(Dispatchers.Main).launch {
            val authResult = withContext(Dispatchers.IO) {
                authRemoteRepositoryImpl.signUp(email, password)
            }
            try {
                if (authResult.errorMessage != null) {
                    authUIState.value = UIState(
                        loading = false,
                        errors = LoginErrors(
                            passwordError = authResult.errorMessage
                        )
                    )
                } else {
                    authUIState.value = UIState(
                        loading = false,
                        data = authResult.data,
                        errors = null
                    )
                }
            } catch (e: Exception) {
                authUIState.value = UIState(
                    loading = false,
                    errors = LoginErrors(communicationError = "Communication Error")
                )
            }
        }
    }

}