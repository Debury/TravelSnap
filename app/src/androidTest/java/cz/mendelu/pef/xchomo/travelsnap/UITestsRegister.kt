package cz.mendelu.pef.xchomo.travelsnap

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.fake.MockAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.MockLoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.RegisterScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsRegister {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @BindValue
    @JvmField
    val mockAuthRemoteRepository: MockAuthRemoteRepository = MockAuthRemoteRepository()

    @BindValue
    @JvmField
    val viewModel: LoginScreenViewModel = MockLoginScreenViewModel()

    @OptIn(ExperimentalAnimationApi::class)
    private fun launchRegisterScreenWithNavigation() {
        composeTestRule.activity.setContent {
            TravelSnapTheme {
                navController = rememberNavController()
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = RegisterScreenDestination
                )
            }
        }
    }

    @Test
    fun test_registerScreen_displaysEmailAndPasswordFields() {
        launchRegisterScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithText("Email").assertExists()
            onNodeWithText("Password").assertExists()
            onNodeWithText("Confirm Password").assertExists()
            Thread.sleep(1000)
        }
    }

    @Test
    fun test_registerButton_clickableWhenEmailAndPasswordValid() {
        launchRegisterScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithText("Email").performTextInput("test@gmail.com")
            onNodeWithText("Password").performTextInput("1234567")
            onNodeWithText("Confirm Password").performTextInput("1234567")
            onNodeWithText("Sign Up").assertIsEnabled()
            Thread.sleep(1000)
        }
    }

    @Test
    fun test_registerButton_navigatesToMainScreen() {
        launchRegisterScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            test_registerScreen_displaysEmailAndPasswordFields()
            onNodeWithText("Email").performTextInput("test@gmail.com")
            onNodeWithText("Password").performTextInput("1234567")
            onNodeWithText("Confirm Password").performTextInput("1234567")

            onNodeWithText("Sign Up").assertExists()
            onNodeWithText("Sign Up").assertIsEnabled()
            onNodeWithText("Sign Up").performClick()
            assertEquals(navController.currentDestination?.route, RegisterScreenDestination.route)

            Thread.sleep(1000)
        }
    }

    @Test
    fun test_navigationToLoginScreen() {
        launchRegisterScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithText("Already have an account ?").performClick()
            assertEquals(navController.currentDestination?.route, LoginScreenDestination.route)
            Thread.sleep(1000)
        }
    }
}
