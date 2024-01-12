package cz.mendelu.pef.xchomo.travelsnap.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.UserData
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomButton
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomTextField
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginErrors
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreenViewModel

import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin
import kotlinx.coroutines.delay

@Destination
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()

    val uiState: MutableState<UIState<UserData, LoginErrors>> =
        rememberSaveable {
            mutableStateOf(UIState())
        }

    viewModel.authUIState.value.let {
        uiState.value = it
    }

    LaunchedEffect(uiState.value.errors != null){
        delay(5000)
        viewModel.clearError()
    }
   BaseScreen(topBarText = null, drawFullScreenContent = true, showLoading = false, placeholderScreenContent =
       if(uiState.value.errors != null) {
           PlaceholderScreenContent(
               null,
               stringResource(id = R.string.register_failed)
           )
       }else{
           null
       }
   ) {
       if(uiState.value.data != null) {
           navigator.navigate(MainScreenDestination())
       }
        RegisterScreenContent(navigator,viewModel,uiState.value)
   }
}

@Composable
fun RegisterScreenContent(
    navigator: DestinationsNavigator,
    viewModel: LoginScreenViewModel,
    uiState: UIState<UserData, LoginErrors>
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isRegisterEnabled = email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                .padding(bottom = halfMargin())
        )

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(id = R.string.password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = halfMargin())
        )

        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = stringResource(id = R.string.confirm_password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = basicMargin())
        )

        CustomButton(
            text = stringResource(id = R.string.sign_up),
            onClick = {
                viewModel.signUp(email, password)
            },
            enabled = isRegisterEnabled
        )

        Spacer(modifier = Modifier.height(basicMargin()))

        TextButton(onClick = {
            navigator.navigate(LoginScreenDestination())
        }) {
            Text(
                text = stringResource(id = R.string.already_have_an_account),
                color = AppColors.AccentColor,
                style = CustomTypography.bodySmall
            )
        }
    }
}




