package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin

const val TestLoginEmailTag = "TestLoginEmailTag"
const val TestLoginPasswordTag = "TestLoginPasswordTag"
const val TestSearchPlaceTag = "SearchField"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    placeholder: String?,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    testTag: String? = null,
    onClick: (() -> Unit)? = null,

) {
    var isFocused by remember { mutableStateOf(false) }

    if (onClick != null){
        Box(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(AppColors.PrimaryColor)
                .padding(vertical = halfMargin(), horizontal = basicMargin()),
               contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = placeholder ?: "",
                    color = if (isFocused) AppColors.DarkTextColor else AppColors.DarkTextColor,
                    style = CustomTypography.labelMedium
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar Icon",
                    tint = if (isFocused) AppColors.DarkTextColor  else AppColors.DarkTextColor,
                    modifier = Modifier.clickable { onClick() }
                )
            }
        }
    }else {
        Column(
            modifier = modifier
                .clickable { onClick?.invoke() }
                .padding(vertical = halfMargin(), horizontal = basicMargin()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (onValueChange != null) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        if (placeholder != null) {
                            Text(placeholder)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .testTag(testTag ?: "")
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.AccentColor,
                        unfocusedBorderColor = AppColors.LightPrimaryColor,
                        focusedLabelColor = AppColors.DarkTextColor,
                        unfocusedLabelColor = AppColors.DarkTextColor,
                        cursorColor = AppColors.AccentColor,
                        containerColor = Color.Transparent,

                    ),
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    singleLine = true,
                    label = {
                        if (placeholder != null) {
                            Text(
                                text = placeholder,
                                color = AppColors.AccentColor,
                                style = CustomTypography.labelMedium
                            )
                        }
                    },

                    )
            }

        }
    }
}





