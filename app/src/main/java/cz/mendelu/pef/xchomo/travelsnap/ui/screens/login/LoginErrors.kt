package cz.mendelu.pef.xchomo.travelsnap.ui.screens.login

data class LoginErrors(
    val communicationError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
)
