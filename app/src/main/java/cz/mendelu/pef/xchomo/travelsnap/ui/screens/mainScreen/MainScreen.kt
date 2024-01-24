package cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.JsonObject
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.datastore.BitmapDataStore
import cz.mendelu.pef.xchomo.travelsnap.datastore.DateDataStore
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomBottomAppBar
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.ListItem
import cz.mendelu.pef.xchomo.travelsnap.model.ListItemData
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.ConfirmDeletionDialog
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomButton
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomDatePicker
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.LoadingScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddEditScreenPlaceDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.LoginScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MapScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import java.io.ByteArrayOutputStream
import java.time.LocalDate

const val PlaceTag = "PlaceTag"
const val ListTag = "ListTag"

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val viewModel: MainScreenViewModel = hiltViewModel()
    val placesArray: Array<Place> = emptyArray()
    var onPlaceHolderClick: () -> Unit = {}
    var showConfirmationDialog: MutableState<Boolean> = rememberSaveable{
        mutableStateOf(false)
    }
    viewModel.placeIdUIState.value.data.let {
        val placesArray: Array<Place>? = viewModel.placesUIState.value.data?.toTypedArray()
        Log.d("MainScreen", "placesArray: $placesArray")
        if (placesArray != null) {
            if (placesArray.isNotEmpty()) {
                onPlaceHolderClick = {
                    navigator.navigate(MapScreenDestination(placesArray, displayPolyline = true))
                }
            } else {
                onPlaceHolderClick = {
                    Toast.makeText(context, "No places to display!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BaseScreen(
        topBarText = null,
        drawFullScreenContent = true,
        showLoading = viewModel.visionUIState.value.loading,
        placeholderScreenContent = null,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(PlaceTag),
                onClick = onPlaceHolderClick,
                containerColor = AppColors.PrimaryColor,
                contentColor = AppColors.DarkTextColor,
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Place"
                )
            }
        },
        onIconClick = {
            navigator.navigate(AddPlaceScreenDestination)
        },
        topBarAction = {
            IconButton(onClick = {
                showConfirmationDialog.value = true
            },
                modifier = Modifier.testTag("Logout")){
                Icon(Icons.Filled.Logout, contentDescription = "Logout", tint = AppColors.DarkTextColor)
            }
        },
        bottomAppBar = { CustomBottomAppBar(navigator, "home") }) {
        if (showConfirmationDialog.value){
            ConfirmDeletionDialog(onConfirm = {

                viewModel.logout()
                navigator.navigate(LoginScreenDestination)
                showConfirmationDialog.value = false
            }, onDismiss = {
                showConfirmationDialog.value = false
            },
                title = stringResource(R.string.logout),
                text = stringResource(R.string.do_you_want_to_logout),
                testTag = "LogoutDialog",
                testTagYes = "LogoutDialogYes",
                testTagNo = "LogoutDialogNo")
        }
        MainScreenContent(navigator, viewModel, it)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenContent(
    navigator: DestinationsNavigator,
    viewModel: MainScreenViewModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val selectedDate by DateDataStore.getSelectedDate(context)
        .collectAsState(initial = null)

    var dontNavigate: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        DateDataStore.saveSelectedDate(context, LocalDate.now())
    }
    LaunchedEffect(selectedDate) {
        val dateToLoad = selectedDate ?: LocalDate.now()
        viewModel.loadPlacesForSelectedDate(dateToLoad)

    }

    val uiState: MutableState<UIState<List<Place>, Int>> =
        rememberSaveable {
            mutableStateOf(UIState(loading = true))
        }

    var showDialog: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }


    val image = BitmapDataStore.getSelectedBitmap(context)
        .collectAsState(initial = null)

    if (image.value != null) {
        Log.d("MainScreenContent", "${image.value}")
        viewModel.visionUIState.value.loading = true
        LaunchedEffect(Unit) {
            if (image.value != null) {
                viewModel.checkImageWithVisionApi(image.value!!)
            }
        }


    }


    viewModel.placesUIState.value.let {
        uiState.value = it

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (viewModel.visionUIState.value.loading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.padding(basicMargin()))

        CustomDatePicker()
        when {
            viewModel.placeIdUIState.value.data != null -> {
                if (!showDialog.value && !dontNavigate.value) {
                    LaunchedEffect(Unit) {
                        BitmapDataStore.clearSelectedBitmap(context)
                        navigator.navigate(AddEditScreenPlaceDestination(viewModel.placeIdUIState.value.data!!))
                    }
                }
            }

            viewModel.placeIdUIState.value.errors != null -> {
                LaunchedEffect(Unit) {
                    BitmapDataStore.clearSelectedBitmap(context)
                    viewModel.visionUIState.value.loading = false
                }
                try {
                    Toast.makeText(context, "Landmark hasn't been found!", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Log.e("MainScreenContent", "Error showing Toast", e)
                }

            }
        }
        when {
            uiState.value.loading -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.loading),
                    subtitle = stringResource(R.string.please_wait)
                ),
                viewModel = null,
                onIconClick = {}
            )

            uiState.value.errors != null -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.error),
                    subtitle = stringResource(id = uiState.value.errors!!)
                ),
                viewModel = null,
                onIconClick = {}
            )

            uiState.value.data == null -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.no_data),
                    subtitle = stringResource(R.string.no_data_to_display)
                ),
                viewModel = null,
                onIconClick = {}
            )

            uiState.value.data?.isNotEmpty() == true -> LazyColumn {
                uiState.value.data?.forEach {
                    item {
                        ListItem(
                            item = ListItemData(
                                image = null,
                                title = it.name ?: stringResource(R.string.no_name),
                                subtitle = it.address ?: stringResource(R.string.no_address)
                            ),
                            viewModel = viewModel,
                            onIconClick = {
                                navigator.navigate(
                                    AddEditScreenPlaceDestination(
                                        place = null,
                                        placeEdit = it.databaseId ,
                                    )
                                )
                            },
                            icon = Icons.Filled.MoreVert
                        )
                    }
                }
            }

            else -> LazyColumn {
                item {
                    ListItem(
                        item = ListItemData(
                            image = null,
                            title = stringResource(R.string.no_data),
                            subtitle = stringResource(R.string.no_data_to_display)
                        ),
                        viewModel = null,
                        onIconClick = {}
                    )
                }
            }
        }

    }

}