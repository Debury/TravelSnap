@file:OptIn(ExperimentalMaterial3Api::class)
package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors.basicTextColor
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors.getBackgroundColor
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors.getTintColor
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.basicMargin
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseScreen(
    topBarText: String?,
    onBackClick: (() -> Unit)? = null,
    showSidePadding: Boolean = true,
    drawFullScreenContent: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showLoading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    onIconClick: (() -> Unit)? = null,
    bottomAppBar: @Composable () -> Unit = {},
    topBarAction: @Composable (() -> Unit)? = null,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {

    Scaffold(
        contentColor = AppColors.getBackgroundColor(),
        containerColor = AppColors.BackgroundColor,
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(4.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        if (topBarText != null) {
                            Text(
                                text = topBarText,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.basicTextColor(),
                                modifier = Modifier
                                    .padding(start = 0.dp)
                                    .weight(1.5f)
                            )
                        }
                    }
                },
                actions = {
                    if (onIconClick != null) {
                        IconButton(onClick = onIconClick, modifier=Modifier.testTag("Add")) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = AppColors.DarkTextColor
                            )
                        }
                    }
                    if (topBarAction != null) {
                        topBarAction()
                    }
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = AppColors.DarkTextColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.DarkPrimaryColor
                )
            )
        },
        bottomBar = {
            bottomAppBar()
        },
    ) {
        if (placeholderScreenContent != null) {
            PlaceHolderScreen(modifier = Modifier.padding(it), content = placeholderScreenContent)
        } else if (showLoading) {
            LoadingScreen(modifier = Modifier.padding(it))
        } else {
            if (!drawFullScreenContent) {
                LazyColumn(modifier = Modifier.padding(it)) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .padding(if (!showSidePadding) basicMargin() else 0.dp)
                        ) {
                            content(it)
                        }
                    }
                }
            } else {
                content(it)
            }
        }
    }
}