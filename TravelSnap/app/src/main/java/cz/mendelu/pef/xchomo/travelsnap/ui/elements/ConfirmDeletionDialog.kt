package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.xchomo.travelsnap.R

@Composable
fun ConfirmDeletionDialog(onConfirm: () -> Unit, onDismiss: () -> Unit, title: String = stringResource(
    R.string.confirm_deletion
), text: String = stringResource(R.string.are_you_sure_you_want_to_delete_this_place), testTag: String = "ConfirmationDialog", testTagYes: String = "ConfirmationDialogYes", testTagNo: String = "ConfirmationDialogNo"
) {
    AlertDialog(
        modifier = Modifier.testTag(testTag),
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(testTagYes),
                onClick = { onConfirm() }
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.testTag(testTagNo),
                onClick = { onDismiss() }
            ) {
                Text(stringResource(R.string.no))
            }
        }
    )
}
