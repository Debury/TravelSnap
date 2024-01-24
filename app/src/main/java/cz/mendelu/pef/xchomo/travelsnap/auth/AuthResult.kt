package cz.mendelu.pef.xchomo.travelsnap.auth

import com.google.firebase.auth.FirebaseUser

sealed class AuthResult<out R> {

    data class Success<out R>(val result: R) : AuthResult<R>()
    data class Error(val error: String) : AuthResult<Nothing>()

    object Loading : AuthResult<Nothing>()

}