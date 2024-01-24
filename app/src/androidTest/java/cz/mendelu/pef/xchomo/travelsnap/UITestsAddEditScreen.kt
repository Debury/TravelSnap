package cz.mendelu.pef.xchomo.travelsnap

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddEditScreenPlaceDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AddEditScreenPlaceUITest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: NavHostController

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testUIElements() {
        launchAddEditScreen()
        with(composeTestRule){
            //TODO: add test for UI elements
        }
    }

    fun launchAddEditScreen() {

        composeTestRule.activity.setContent {
            TravelSnapTheme {
                navController = rememberNavController()
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = AddEditScreenPlaceDestination
                )
            }
        }
    }
}