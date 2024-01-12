package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.model.ListItemData
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography

@Composable
fun ListItem(
    item: ListItemData,
    viewModel: BaseViewModel?,
    onIconClick : () -> Unit?,
    icon: ImageVector? = null,
    testTag: String? = null){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .testTag(testTag ?: "")


    ) {
        Column(
            modifier = Modifier.background(AppColors.LightPrimaryColor)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                item.image?.let {
                    Image(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = item.title, color = AppColors.LightTextColor, style = CustomTypography.titleMedium)
                    Text(text = item.subtitle, color = AppColors.SecondaryTextColor)
                }
                IconButton(onClick = { onIconClick() }) {
                    Icon(
                        imageVector = if (icon != null) {
                            icon!!
                        } else {
                            Icons.Default.MoreVert
                        },
                        contentDescription = "Action",
                        tint = AppColors.LightTextColor
                    )
                }

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(AppColors.AccentColor)
                    .shadow(4.dp, shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            )
        }




    }
}
