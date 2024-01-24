package cz.mendelu.pef.xchomo.travelsnap.ui.screens.login

import android.app.Activity.RESULT_OK
import android.graphics.fonts.FontStyle
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.ConfirmDeletionDialog
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomButton
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomTextField
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.GoogleSignInButton
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginButtonTag
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginEmailTag
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginPasswordTag
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.RegisterScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay



@Destination(start = true)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val uiState: MutableState<UIState<UserData, LoginErrors>> =
        rememberSaveable {
            mutableStateOf(UIState())
        }


    val showError: MutableState<Boolean> = rememberSaveable{
        mutableStateOf(false)
    }

    viewModel.authUIState.value.let {
        uiState.value = it
    }
    BaseScreen(topBarText = null, drawFullScreenContent = true, showLoading = false) {
        if (viewModel.authUIState.value.data != null) {
            navigator.navigate(MainScreenDestination)
        }
        LoginScreenContent(navigator,viewModel)
    }


}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LoginScreenContent(
    navigator: DestinationsNavigator,
    viewModel: LoginScreenViewModel
){
    val customScope = CoroutineScope(Dispatchers.Main)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isSignInEnabled = email.isNotEmpty() && password.isNotEmpty()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
            .padding(basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(id = R.string.email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = halfMargin()),
            testTag = TestLoginEmailTag
        )

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(id = R.string.password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = basicMargin()),
            testTag = TestLoginPasswordTag

        )
        if (viewModel.authUIState.value.errors != null) {
            when(val error = viewModel.authUIState.value.errors){
                is LoginErrors -> {
                    when{
                        error.usernameError != null -> {
                            errorMessage = stringResource(R.string.invalid_email)
                        }
                        error.passwordError != null -> {
                            errorMessage = stringResource(R.string.invalid_password)
                        }
                        error.communicationError != null -> {
                            errorMessage = stringResource(R.string.check_your_network_connection)
                        }
                    }
                }
            }
            if(errorMessage != null){
                Text(
                    text = errorMessage!!,
                    style = CustomTypography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        CustomButton(text = stringResource(id = R.string.sign_in), onClick = {
            viewModel.signInWithEmailAndPassword(email, password)
        }, enabled = isSignInEnabled, testTag = TestLoginButtonTag )


        GoogleSignInButton {
            //TODO: fix this
        }

        Spacer(modifier = Modifier.height(basicMargin()))
        TextButton(onClick = { /*TODO*/ }) {
            Text(stringResource(id = R.string.forgot_password), color = AppColors.AccentColor, style = CustomTypography.bodySmall)
        }
        TextButton(onClick = {
            navigator.navigate(RegisterScreenDestination)
        }) {
            Text(text = stringResource(id = R.string.sign_up), color = AppColors.AccentColor, style = CustomTypography.bodySmall)
        }
    }
}




