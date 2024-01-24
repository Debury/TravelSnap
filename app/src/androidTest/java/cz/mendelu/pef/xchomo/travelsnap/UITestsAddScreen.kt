package cz.mendelu.pef.xchomo.travelsnap

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.fake.FakeLocationManagerImpl
import cz.mendelu.pef.xchomo.travelsnap.fake.MockAddScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.fake.MockAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.fake.MockLoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestSearchPlaceTag
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.login.LoginScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsAddScreen {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)


    @BindValue
    @JvmField
    val mockAuthRemoteRepository: FakeLocationManagerImpl = FakeLocationManagerImpl()

    @BindValue
    @JvmField
    val viewModel: AddPlaceViewModel = MockAddScreenViewModel()

    @Before
    fun setUp() {
        hiltRule.inject()




    }



    @Test
    fun testInitialState() {
        launchAddPlaceScreenWithNavigation()

        with(composeTestRule){
            composeTestRule.onNodeWithTag(TestSearchPlaceTag).assertExists()
            composeTestRule.onNodeWithTag("ListItem").assertExists()
            Thread.sleep(2000)
        }

    }

    @Test
    fun testSearchPlace_Correct() {
        launchAddPlaceScreenWithNavigation()

        with(composeTestRule){
            onNodeWithTag(TestSearchPlaceTag).performTextInput("Brno")
            waitForIdle()
            onNodeWithTag("ListItem").assertExists()
            Thread.sleep(2000)
        }

    }

    @Test
    fun testSearchPlace_Incorrect() {
        launchAddPlaceScreenWithNavigation()

        with(composeTestRule){
            onNodeWithTag(TestSearchPlaceTag).performTextInput("Baba")
            onNodeWithTag("ListItemCandidate").assertExists()
            Thread.sleep(2000)
        }

    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun launchAddPlaceScreenWithNavigation() {
        composeTestRule.activity.setContent {
            TravelSnapTheme {
                navController = rememberNavController()
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = AddPlaceScreenDestination
                )
            }
        }
    }
}