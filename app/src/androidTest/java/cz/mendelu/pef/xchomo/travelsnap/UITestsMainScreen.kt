package cz.mendelu.pef.xchomo.travelsnap

import MockMainScreenViewModel
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.fake.FakePlacesRepositoryImpl

import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BottomAppBarTag
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.RegisterScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.ListTag
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.PlaceTag
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsMainScreen {

    private lateinit var navController: NavHostController

    @BindValue
    @JvmField
    val mockPlaces: FakePlacesRepositoryImpl = FakePlacesRepositoryImpl()

    @BindValue
    @JvmField
    val viewModel: MainScreenViewModel = MockMainScreenViewModel()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }





    @Test
    fun test_mainScreen_displaysFloatingActionButton() {
        launchMainScreenWithNavigation()
        composeRule.onNodeWithTag(PlaceTag).assertExists().assertIsDisplayed()
    }

    @Test
    fun test_mainScreen_displaysBottomAppBar() {
        launchMainScreenWithNavigation()
        composeRule.onNodeWithTag(BottomAppBarTag).assertExists().assertIsDisplayed()
    }

    @Test
    fun test_mainScreen_listItemsDisplayed() {
        launchMainScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag(ListTag).assertIsDisplayed()
        }
    }

    @Test
    fun test_mainScreen_addButtonInToolbarNavigation() {
        launchMainScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("Add").performClick()
            assertEquals(navController.currentDestination?.route, AddPlaceScreenDestination.route)
            Thread.sleep(2000)
        }
    }

    @Test
    fun test_mainScreen_logoutButtonInToolbarNavigationYes() {
        launchMainScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("Logout").performClick()
            onNodeWithTag("LogoutDialog").assertIsDisplayed()
            onNodeWithTag("LogoutDialogYes").performClick()
            assertEquals(navController.currentDestination?.route, LoginScreenDestination.route)
            Thread.sleep(2000)
        }
    }

    @Test
    fun test_mainScreen_logoutButtonInToolbarNavigationNo() {
        launchMainScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("Logout").performClick()
            onNodeWithTag("LogoutDialog").assertIsDisplayed()
            onNodeWithTag("LogoutDialogNo").performClick()
            assertEquals(navController.currentDestination?.route, MainScreenDestination.route)
            Thread.sleep(2000)
        }
    }
    @OptIn(ExperimentalAnimationApi::class)
    private fun launchMainScreenWithNavigation() {
        composeRule.activity.setContent {
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
}