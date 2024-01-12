package cz.mendelu.pef.xchomo.travelsnap

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.CameraScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MapScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
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
class UITestsCustomBottomAppBar {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

    }


    @OptIn(ExperimentalAnimationApi::class)
    private fun launchHomeScreenWithNavigation() {
        composeTestRule.activity.setContent {
            TravelSnapTheme {
                navController = rememberNavController()
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = MainScreenDestination
                )
            }
        }
    }


    @Test
    fun testNavigateToHome() {
        launchHomeScreenWithNavigation()
        with(composeTestRule){
            onNodeWithTag("Home").performClick()
            assertEquals(navController.currentDestination?.route, MainScreenDestination.route)
            Thread.sleep(2000)
        }
    }

    @Test
    fun testNavigateToMap() {
        launchHomeScreenWithNavigation()
        with(composeTestRule){
            onNodeWithTag("Map").performClick()
            assertEquals(navController.currentDestination?.route, MapScreenDestination.route)
            Thread.sleep(2000)
        }
    }

    @Test
    fun testNavigateToCamera() {
        launchHomeScreenWithNavigation()
        with(composeTestRule){
            onNodeWithTag("Camera").performClick()
            assertEquals(navController.currentDestination?.route, CameraScreenDestination.route)
            Thread.sleep(2000)
        }

    }
}