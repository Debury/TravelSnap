package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.marosseleng.compose.material3.datetimepickers.date.domain.DatePickerDefaults
import com.marosseleng.compose.material3.datetimepickers.date.domain.DatePickerStroke
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.datastore.DateDataStore
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomDatePicker(
    selectedDatePlace: LocalDate? = null,
)
{

    val context = LocalContext.current
    val selectedDate by DateDataStore.getSelectedDate(context)
        .collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    if(selectedDatePlace != null){
        LaunchedEffect(Unit){
            DateDataStore.saveSelectedDate(context, selectedDatePlace)
        }
    }

    val name = remember {
        mutableStateOf("")
    }

    val datePickerColors = DatePickerDefaults.colors(
        dialogSingleSelectionTitleTextColor = AppColors.PrimaryColor,
        headlineSingleSelectionTextColor = AppColors.PrimaryColor,
        weekDayLabelTextColor = AppColors.DarkPrimaryColor,
        previousMonthDayLabelTextColor = AppColors.DarkPrimaryColor,
        monthDayLabelSelectedTextColor = AppColors.DarkTextColor,
        monthDayLabelSelectedBackgroundColor = AppColors.PrimaryColor,
        todayLabelTextColor = AppColors.LightTextColor,
        todayLabelBackgroundColor = Color.Transparent,
        todayStroke = DatePickerStroke(1.dp, AppColors.PrimaryColor),
        selectedYearTextColor = AppColors.DarkTextColor,
        selectedYearBackgroundColor = AppColors.PrimaryColor,
        selectedMonthTextColor = AppColors.DarkTextColor,
        selectedMonthBackgroundColor = AppColors.PrimaryColor,
        dialogDividerStroke = DatePickerStroke(1.dp, AppColors.PrimaryColor),


    )
    val buttonColors = ButtonDefaults.textButtonColors(
        containerColor = Color.Transparent,
        contentColor = AppColors.LightTextColor,
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            onDateChange = {  date ->
                CoroutineScope(Dispatchers.IO).launch {
                    DateDataStore.saveSelectedDate(context, date)
                }
                showDialog = false},
            modifier = Modifier.testTag("DatePicker"),
            initialDate = selectedDate,
            locale = LocalConfiguration.current.locales[0],
            today = LocalDate.now(),
            showDaysAbbreviations = true,
            highlightToday = true,
            colors = datePickerColors,
            shapes = DatePickerDefaults.shapes(),
            typography = DatePickerDefaults.typography(),
            buttonColors = buttonColors,
            shape = RoundedCornerShape(16.dp),
            containerColor = AppColors.BackgroundColor,
            titleContentColor = AppColors.PrimaryTextColor,
            tonalElevation = 4.dp,

        )
    }

    Column {
        CustomTextField(
            value = selectedDate.toString(),
            onValueChange = {  } ,
            placeholder = selectedDate.toString(),
            onClick = {
                showDialog = true
            })
    }

}