package cz.mendelu.pef.xchomo.travelsnap.ui.screens.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.credentials.IdToken

interface LoginScreenActions {
    fun signInWithEmailAndPassword(email: String, password: String)
    fun signOut()
    fun signUp(email: String, password: String)

}