package cz.mendelu.pef.xchomo.travelsnap.ui.screens.addedit

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.datastore.DateDataStore
import cz.mendelu.pef.xchomo.travelsnap.datastore.TimeDataStore
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.ConfirmDeletionDialog
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomBottomAppBar
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomButton
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomDatePicker
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomTextField
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomTimePickerDialog
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.IconSelectorRow
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PhotoList
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin
import org.junit.runner.Description
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AddEditScreenPlace(
    navigator: DestinationsNavigator,
    place: String? = null,
    placeEdit: Long? = null
) {
    val viewModel: AddEditScreenViewModel = hiltViewModel()
    val context = LocalContext.current


    val iconOptions = listOf(R.drawable.bridge_svgrepo_com, R.drawable.castle_12_svgrepo_com, R.drawable.park_svgrepo_com, R.drawable.church_2_svgrepo_com, R.drawable.museum_svgrepo_com, R.drawable.lighthouse_on_svgrepo_com, R.drawable.stadium_svgrepo_com, R.drawable.palace_svgrepo_com, R.drawable.skyscraper_svgrepo_com, R.drawable.stone_bust_svgrepo_com, R.drawable.university_svgrepo_com)
    var name: String? = null
    var description: String? = null
    var selectedIconIndex: Int? = null
    var selectedTime: Long? = null
    var selectedDate: LocalDate? = null
    if (placeEdit != null) {
        if(viewModel.placeUIState.value.data != null) {
            name = viewModel.placeUIState.value.data!!.name ?: null
            description = viewModel.placeUIState.value.data!!.description ?: null
            selectedIconIndex = iconOptions.indexOf(viewModel.placeUIState.value.data!!.selectedIconId)
            selectedTime = viewModel.placeUIState.value.data!!.time
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            selectedDate = LocalDate.parse(viewModel.placeUIState.value.data!!.selectedDate,formatter)

            viewModel.placeUIState.value.loading = false
            Log.d("AddEditScreenPlace", "name: $selectedTime")
            Log.d("AddEditScreenPlace", "name: $selectedIconIndex")

        }
    }
    var showDeleteDialog = remember { mutableStateOf(false) }
    LaunchedEffect(place, placeEdit) {
        if (placeEdit != null) {
            viewModel.getPlaceById(placeEdit)
        }
        if (place != null) {
            viewModel.getPlaceDetails(place)
        }
    }

    BaseScreen(
        topBarText = null,
        drawFullScreenContent = true,
        showLoading = viewModel.placeUIState.value.loading,
        placeholderScreenContent = null,
        bottomAppBar = { CustomBottomAppBar(navigator, "home") },
        topBarAction = {
            if (placeEdit != null) {
                IconButton(onClick = { showDeleteDialog.value = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = AppColors.DarkTextColor
                    )
                }
            }
        },
        onBackClick = {
            navigator.navigate(AddPlaceScreenDestination)
        }
    ) {
        if (!viewModel.placeUIState.value.loading) {
            AddEditScreenPlaceContent(
                place = place,
                paddingValues = it,
                viewModel = viewModel,
                name = name,
                description = description,
                selectedIconIndex = selectedIconIndex,
                placeEdit = placeEdit,
                selectedTimePlace = selectedTime,
                selectedDatePlace = selectedDate,
                navigator = navigator
            )
        }
        if(showDeleteDialog.value){
            ConfirmDeletionDialog(
                onConfirm = {
                    viewModel.deletePlace(placeEdit!!)
                    Toast.makeText(context, "Place deleted!", Toast.LENGTH_SHORT).show()
                    navigator.navigate(MainScreenDestination)
                },
                onDismiss = {
                    showDeleteDialog.value = false
                },
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditScreenPlaceContent(
    place: String?,
    paddingValues: PaddingValues,
    placeEdit: Long?,
    viewModel: AddEditScreenViewModel,
    name: String?,
    description: String?,
    selectedIconIndex: Int?,
    selectedTimePlace: Long?,
    selectedDatePlace: LocalDate?,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    var name: MutableState<String?> = remember { mutableStateOf(name) }
    var description: MutableState<String?> = remember { mutableStateOf(description) }
    var selectedIconIndex: MutableState<Int?> = remember { mutableStateOf(selectedIconIndex) }
    val selectedTime by TimeDataStore.getSelectedTime(context)
        .collectAsState(initial = null)
    val selectedTimePlace by remember { mutableStateOf(selectedTimePlace) }
    var selectedIconIdForSave: MutableState<Int?> = remember { mutableStateOf(R.drawable.bridge_svgrepo_com) }
    val selectedDate by DateDataStore.getSelectedDate(context)
        .collectAsState(initial = null)

    var uiState: MutableState<UIState<Place, AddPlaceErrors>> =
        rememberSaveable {
            mutableStateOf(UIState())
        }

    LaunchedEffect(Unit) {
        if (place != null){ viewModel.getPlaceDetails(place)}

    }

    viewModel.placeUIState.value.let {
        uiState.value = it
        Log.d("AddEditScreenPlace", "uiState.value: ${uiState.value.data}")
    }

    val iconOptions = listOf(R.drawable.bridge_svgrepo_com, R.drawable.castle_12_svgrepo_com, R.drawable.park_svgrepo_com, R.drawable.church_2_svgrepo_com, R.drawable.museum_svgrepo_com, R.drawable.lighthouse_on_svgrepo_com, R.drawable.stadium_svgrepo_com, R.drawable.palace_svgrepo_com, R.drawable.skyscraper_svgrepo_com, R.drawable.stone_bust_svgrepo_com, R.drawable.university_svgrepo_com)



    when (uiState.value.loading) {
        true -> {
            Column {
                Text(stringResource(R.string.loading))
            }
        }

        false -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                verticalArrangement = Arrangement.spacedBy(halfMargin())
            ) {
                item {
                    Spacer(modifier = Modifier.size(halfMargin()))
                }

                item {
                    CustomTextField(
                        value = name.value ?: uiState.value.data?.name ?: "" ,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = stringResource(R.string.name),
                        testTag = "Name"
                    )
                }

                item {
                    CustomDatePicker(selectedDatePlace = selectedDatePlace)
                }

                item{
                    CustomTimePickerDialog(
                        onDismissRequest = {
                            Log.d("AddEditScreenPlace", "selectedDate: $selectedDate")
                        },
                        selectedTimePlace = selectedTimePlace?.let { LocalTime.ofSecondOfDay(it) }
                    )
                }

                item {
                    IconSelectorRow(iconOptions = iconOptions, savedSelectedIndex = selectedIconIndex.value) { selectedIconId ->
                        selectedIconIdForSave.value = selectedIconId
                    }

                }

                if (uiState.value.data?.photos?.isNotEmpty() == true) {
                    val apiKey = context.getString(R.string.google_maps_key)
                    var listOfPhotos: List<String> = emptyList()

                    for (photoUrl in uiState.value.data!!.photos!!) {
                        val photoReference = photoUrl.photoReference
                        val imageUrl =
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&maxheight=800&photo_reference=$photoReference&key=$apiKey"
                        listOfPhotos = listOfPhotos + imageUrl
                    }

                    item {
                        PhotoList(photoUrls = listOfPhotos)
                    }
                }

                item {
                    CustomTextField(
                        value = description.value ?: uiState.value.data?.description ?: "",
                        onValueChange = {
                            description.value = it
                        },
                        placeholder = stringResource(R.string.description),
                        testTag = "Description"

                    )
                }

                item {
                    CustomButton(
                        text = if(place!=null) {
                            stringResource(R.string.add_place)
                        } else {
                            stringResource(R.string.save)
                        },
                        onClick = {
                            if(place != null) {
                                Log.d("AddEditScreenPlace", "selectedDate: $name")
                                viewModel.insertPlace(
                                    Place(
                                        id = uiState.value.data?.id ?: "",
                                        address = uiState.value.data?.address ?: "",
                                        name = name.value ?: uiState.value.data?.name ?: "",
                                        rating = uiState.value.data?.rating ?: 0.0,
                                        geometry = uiState.value.data?.geometry,
                                        photos = uiState.value.data?.photos,
                                        selectedDate = selectedDate.toString(),
                                        description = description.value ?: uiState.value.data?.description ?: "",
                                        time = selectedTime,
                                        selectedIconId = if (selectedIconIndex.value == 0) R.drawable.bridge_svgrepo_com else selectedIconIdForSave.value
                                    )
                                )
                            }else{
                                Log.d("AddEditScreenPlace", "name: $name")
                                viewModel.updatePlace(
                                    Place(
                                        databaseId = placeEdit ?: 0,
                                        id = uiState.value.data?.id ?: "",
                                        address = uiState.value.data?.address ?: "",
                                        name = name.value ?: uiState.value.data?.name ?: "" ,
                                        rating = uiState.value.data?.rating ?: 0.0,
                                        geometry = uiState.value.data?.geometry,
                                        photos = uiState.value.data?.photos,
                                        selectedDate = selectedDate.toString(),
                                        description = description.value ?: uiState.value.data?.description ?: "",
                                        time = selectedTime,
                                        selectedIconId =  selectedIconIdForSave.value ?: iconOptions[0]
                                    )
                                )
                            }
                            Toast.makeText(context, "Place saved!", Toast.LENGTH_SHORT).show()
                            navigator.navigate(MainScreenDestination)
                        },
                        testTag = "Save",
                        enabled = true)
                }
            }
        }
    }
}




