package cz.mendelu.pef.xchomo.travelsnap.model

import java.io.Serializable

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
) : Serializable