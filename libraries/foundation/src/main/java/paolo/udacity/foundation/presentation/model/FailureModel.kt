package paolo.udacity.foundation.presentation.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


data class FailureModel(
    @StringRes val title: Int = NONE,
    @StringRes val message: Int = NONE,
    @DrawableRes val icon: Int = NONE,
    val code: Int = NONE,
    val localizedMessage: String? = EMPTY_STRING,
    val origin : Throwable
) {

    fun messageForUser(context: Context): String {
        origin.message?.let {
            return it
        }
        return context.getString(message)
    }

    companion object {
        const val NONE = -1
        const val EMPTY_STRING = ""
    }

}