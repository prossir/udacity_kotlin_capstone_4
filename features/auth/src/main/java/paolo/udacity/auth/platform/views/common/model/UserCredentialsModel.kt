package paolo.udacity.auth.platform.views.common.model


data class UserCredentialsModel(
    val email: String,
    val password: String
) {

    companion object {
        fun new() = UserCredentialsModel("", "")
    }

}