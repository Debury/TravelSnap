package cz.mendelu.pef.xchomo.travelsnap.ui.screens.add


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.location.LocationManager
import cz.mendelu.pef.xchomo.travelsnap.model.ListItemData
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomBottomAppBar
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomTextField
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.ListItem
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.TestSearchPlaceTag
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddEditScreenPlaceDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AddPlaceScreen(
    navigator: DestinationsNavigator
) {

    val viewModel: AddPlaceViewModel = hiltViewModel()
    BaseScreen(
        topBarText = null,
        drawFullScreenContent = true,
        showLoading = false,
        placeholderScreenContent = null,
        bottomAppBar = { CustomBottomAppBar(navigator,"home") },
        onBackClick = {
            navigator.navigate(MainScreenDestination)
        }
    ) {
        AddPlaceScreenContent(
            navigator = navigator,
            viewModel = viewModel,
            paddingValues = it
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun AddPlaceScreenContent(
    navigator: DestinationsNavigator,
    viewModel: AddPlaceViewModel,
    paddingValues: PaddingValues
) {
    var searchValue: String by remember { mutableStateOf("") }



    val context = LocalContext.current

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val focusLocationManager = remember {
        LocationManager(context).apply {
            onLocationChanged = { location ->
                currentLocation = location
            }
        }
    }
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(permissionState) {
        permissionState.launchPermissionRequest()
    }
    LaunchedEffect(permissionState.hasPermission) {
        focusLocationManager.startLocationUpdates()
        focusLocationManager.onLocationChanged = {
            if(viewModel.performSearch.value && it != null && searchValue.isEmpty()) {
                viewModel.performNearbySearch(it!!.latitude, it!!.longitude)
                viewModel.performSearch.value = false
            }
        }
    }
    var uiState: MutableState<UIState<PlacesResponse, AddPlaceErrors>>

    viewModel.placesUIState.let {
        uiState = it
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        CustomTextField(
            value = searchValue,
            onValueChange = {
                searchValue = it
                viewModel.searchPlaceFromText(searchValue, "name")
            },
            placeholder = stringResource(R.string.search_name_of_place), testTag = TestSearchPlaceTag
        )
        when {
            uiState.value.loading -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.loading),
                    subtitle = stringResource(R.string.please_wait)
                ),
                viewModel = null,
                onIconClick = {},
                icon = Icons.Default.Add,
                testTag = "ListItemNot"
            )

            uiState.value.errors != null -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.error),
                    subtitle = stringResource(id = uiState.value.errors!!.communicationError)
                ),
                viewModel = null,
                onIconClick = {},
                icon = Icons.Default.Add,
                testTag = "ListItemNot"
            )

            uiState.value.data == null -> ListItem(
                item = ListItemData(
                    image = null,
                    title = stringResource(R.string.no_data),
                    subtitle = stringResource(R.string.no_data_to_display)
                ),
                viewModel = null,
                onIconClick = {},
                icon = Icons.Default.Add,
                testTag = "ListItemNot"
            )

            uiState.value.data?.results  != null -> LazyColumn {
                uiState.value.data?.results?.forEach {
                    item {
                        ListItem(
                            item = ListItemData(
                                image = null,
                                title = it.name ?: stringResource(R.string.no_name),
                                subtitle = it.address ?: stringResource(R.string.no_address)
                            ),
                            viewModel = viewModel,
                            onIconClick = {
                                navigator.navigate(AddEditScreenPlaceDestination(
                                    place = it.id ?: "",
                                ))
                            },
                            icon = Icons.Default.Add,
                            testTag = "ListItem"
                        )
                    }
                }
            }

            else -> LazyColumn{
                uiState.value.data?.candidates?.forEach {
                    item {
                        ListItem(
                            item = ListItemData(
                                image = null,
                                title = it.name ?: stringResource(R.string.no_name),
                                subtitle = it.address ?: stringResource(R.string.no_address),
                            ),
                            viewModel = viewModel,
                            onIconClick = {
                                navigator.navigate(AddEditScreenPlaceDestination(
                                    place = it.id ?: "",
                                ))
                            },
                            testTag = "ListItemCandidate",
                            icon = Icons.Default.Add
                        )
                    }
                }
            }
        }
    }

}

