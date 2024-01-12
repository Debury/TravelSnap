package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.annotation.Dimension
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
const val TestLoginButtonTag = "TestLoginButtonTag"

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier? = null,
    testTag: String? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) AppColors.PrimaryColor else Color.Gray,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(basicMargin())
            .testTag(testTag ?: "")

    ) {
        Text(
            text = text,
            color = if (enabled) AppColors.DarkTextColor else Color.Black,
            style = CustomTypography.labelLarge
        )
    }
}