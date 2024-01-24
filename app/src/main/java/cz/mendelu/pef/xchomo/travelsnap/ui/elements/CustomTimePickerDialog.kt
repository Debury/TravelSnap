package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import android.content.Context

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.marosseleng.compose.material3.datetimepickers.time.domain.TimePickerDefaults
import com.marosseleng.compose.material3.datetimepickers.time.domain.TimePickerStroke
import com.marosseleng.compose.material3.datetimepickers.time.ui.TimePicker
import cz.mendelu.pef.xchomo.travelsnap.datastore.DateDataStore
import cz.mendelu.pef.xchomo.travelsnap.datastore.TimeDataStore
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTimePickerDialog(
    onDismissRequest: () -> Unit,
    selectedTimePlace: LocalTime? = null,
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }

    val selectedTime by TimeDataStore.getSelectedTime(context)
        .collectAsState(initial = null)

    LaunchedEffect(Unit) {
        if (selectedTimePlace != null) {
            Log.d("CustomTimePickerDialog", "selectedTimePlace: $selectedTimePlace")
            val timeInSeconds = selectedTimePlace.toSecondOfDay().toLong()
            TimeDataStore.saveSelectedTime(context, timeInSeconds)

        }
    }
    var storedTime = selectedTime?.let { LocalTime.ofSecondOfDay(it) } ?: LocalTime.now()
    val timePickerColors =
        TimePickerDefaults.colors(
            dialogTitleTextColor = AppColors.PrimaryColor,
            clockDigitsSelectedBackgroundColor = AppColors.PrimaryColor.copy(alpha = 0.24f),
            clockDigitsSelectedTextColor = AppColors.PrimaryColor,
            clockDigitsSelectedBorderStroke = null,
            clockDigitsUnselectedBackgroundColor = AppColors.BackgroundColor,
            clockDigitsUnselectedTextColor = AppColors.LightTextColor,
            clockDigitsUnselectedBorderStroke = null,
            clockDigitsColonTextColor = AppColors.LightTextColor,
            amPmSwitchFieldSelectedBackgroundColor = AppColors.LightPrimaryColor,
            amPmSwitchFieldSelectedTextColor = AppColors.LightPrimaryColor,
            amPmSwitchFieldUnselectedBackgroundColor = AppColors.LightPrimaryColor,
            amPmSwitchFieldUnselectedTextColor = AppColors.LightTextColor,
            amPmSwitchBorderDividerStroke = TimePickerStroke(
                Dp.Hairline,
                AppColors.AccentColor
            ),
            dialCenterColor = AppColors.PrimaryColor,
            dialHandColor = AppColors.PrimaryColor,
            dialBackgroundColor = AppColors.LightPrimaryColor,
            dialNumberSelectedBackgroundColor = AppColors.PrimaryColor,
            dialNumberSelectedTextColor = AppColors.DarkTextColor,
            dialNumberUnselectedBackgroundColor = Color.Transparent,
            dialNumberUnselectedTextColor = AppColors.LightTextColor,
        )
    if (showDialog) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                .size(400.dp, 430.dp)
                .clip(RoundedCornerShape(8.dp))

        ) {
            TimePicker(modifier = Modifier.testTag("TimePicker"),initialTime = selectedTimePlace ?: storedTime, onTimeChange = {
                storedTime = it
                val timeInSeconds = it.toSecondOfDay().toLong()
                CoroutineScope(Dispatchers.IO).launch {
                    TimeDataStore.saveSelectedTime(context, timeInSeconds)
                }
            }, title = {
                Text(text = "")
            }, colors = timePickerColors)
        }
    }

}





