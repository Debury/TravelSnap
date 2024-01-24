package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.CameraScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MainScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.MapScreenDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val BottomAppBarTag = "BottomAppBarTag"
@Composable
fun CustomBottomAppBar(
    navigator: DestinationsNavigator,
    selectedScreen: String,
) {
    val viewModel: CustomBottomAppBarViewModel = hiltViewModel()
    val places: List<Place> by viewModel.getPlaces().collectAsState(listOf())
    val selectedScreenNow = remember { mutableStateOf(selectedScreen) }
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
            .shadow(4.dp)
            .testTag(BottomAppBarTag),
        containerColor = AppColors.DarkPrimaryColor,

    ) {
        Spacer(modifier = Modifier.padding(halfMargin()))
        BottomAppBarItem(
            icon = Icons.Default.Home,
            isSelected = selectedScreenNow.value == "home",
            testTag = "Home",
            onSelected = {
                selectedScreenNow.value = "home"
                navigator.navigate(MainScreenDestination())
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomAppBarItem(
            icon = Icons.Default.Map,
            isSelected = selectedScreenNow.value == "map",
            testTag = "Map",
            onSelected = {
                selectedScreenNow.value = "map"
                navigator.navigate(MapScreenDestination(places.toTypedArray()))
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomAppBarItem(
            icon = Icons.Default.CameraAlt,
            isSelected = selectedScreenNow.value == "camera",
            testTag = "Camera",
            onSelected = {
                selectedScreenNow.value = "camera"
                navigator.navigate(CameraScreenDestination())
            }
        )
        Spacer(modifier = Modifier.padding(halfMargin()))
    }
}

@Composable
fun BottomAppBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String = "",
    onSelected: () -> Unit
) {
    IconButton(
        modifier = Modifier.testTag(testTag),
        onClick = onSelected) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) AppColors.DarkTextColor else AppColors.PrimaryColor
        )
    }
}

@HiltViewModel
class CustomBottomAppBarViewModel @Inject constructor(
    private val placesRepository: PlacesRepositoryImpl
): BaseViewModel() {
    fun getPlaces(): Flow<List<Place>> = placesRepository.getAll()
}