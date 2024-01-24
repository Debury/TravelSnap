package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin

@Composable
fun IconSelectorRow(
    iconOptions: List<Int>,
    savedSelectedIndex: Int? = null,
    onIconSelected: (Int) -> Unit,

) {
    var selectedIconIndex by remember { mutableStateOf(savedSelectedIndex) }

    LazyRow(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .background(color = AppColors.LightPrimaryColor, shape = RoundedCornerShape(8.dp))
            .padding(basicMargin())
            .testTag("IconSelector"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(iconOptions) { iconResId ->
            val isSelected = iconOptions.indexOf(iconResId) == selectedIconIndex

            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        selectedIconIndex = iconOptions.indexOf(iconResId)
                        onIconSelected(iconResId)
                    },
                tint = if (isSelected) AppColors.PrimaryColor else Color.Unspecified
            )
        }
    }
}