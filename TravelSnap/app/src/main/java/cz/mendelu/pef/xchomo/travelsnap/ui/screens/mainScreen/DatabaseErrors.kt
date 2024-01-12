package cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen

import android.provider.Settings.Global.getString
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.xchomo.travelsnap.R

sealed class DatabaseError : Exception() {
    object DatabaseAccessError : DatabaseError()
    object DataNotFound : DatabaseError()
    object DataInsertionError : DatabaseError()
    object DataUpdateError : DatabaseError()
    object DataDeletionError : DatabaseError()

    object SQLLiteException : DatabaseError()
    data class UnexpectedError(val errorMessage: String? = null) : DatabaseError()

    fun getErrorStringResId(): Int {
        return when (this) {
            SQLLiteException -> R.string.error_sqlite
            DatabaseAccessError -> R.string.error_database_access
            DataNotFound -> R.string.error_data_not_found
            DataInsertionError -> R.string.error_data_insertion
            DataUpdateError -> R.string.error_data_update
            DataDeletionError -> R.string.error_data_deletion
            is UnexpectedError -> R.string.error_unexpected
        }
    }
}