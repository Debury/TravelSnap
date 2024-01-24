package cz.mendelu.pef.xchomo.travelsnap

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.MainActivity
import cz.mendelu.pef.xchomo.travelsnap.fake.FakePlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.fake.MockAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.MockLoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginButtonTag
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginEmailTag
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestLoginPasswordTag

import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.RegisterScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsLogin{

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
    private fun launchLoginScreenWithNavigation() {
        composeTestRule.activity.setContent {
            TravelSnapTheme {
                navController = rememberNavController()
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = LoginScreenDestination
                )
            }
        }
    }
    @Test
    fun test_loginScreen_displaysEmailAndPasswordFields() {
        launchLoginScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithTag(TestLoginEmailTag).assertIsDisplayed()
            onNodeWithTag(TestLoginPasswordTag).assertIsDisplayed()
            Thread.sleep(1000)
        }

    }

    @Test
    fun test_signInButton_clickableWhenEmailAndPasswordProvided() {
        launchLoginScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithTag(TestLoginEmailTag).performTextInput("xchomo@mendelu.cz")
            onNodeWithTag(TestLoginPasswordTag).performTextInput("Maskot7011")
            onNodeWithTag(TestLoginButtonTag).assertIsEnabled()
            onNodeWithText("Sign in").performClick()
            waitForIdle()
            assertEquals(navController.currentDestination?.route, MainScreenDestination.route)
            Thread.sleep(1000)
        }
    }


    @Test
    fun test_signInButton_navigatesToRegisterScreen() {
        launchLoginScreenWithNavigation()
        with(composeTestRule) {
            waitForIdle()
            onNodeWithText("Sign Up").performClick()
            assertEquals(navController.currentDestination?.route, RegisterScreenDestination.route)
            Thread.sleep(1000)
        }
    }


}