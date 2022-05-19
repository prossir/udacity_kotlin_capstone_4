package paolo.udacity.auth.platform.views.common.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import paolo.udacity.auth.R
import paolo.udacity.auth.databinding.ActivityAuthenticationBinding
import paolo.udacity.foundation.constants.PackageConstants
import paolo.udacity.foundation.presentation.model.FailureModel
import paolo.udacity.foundation.utils.Event


@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private val viewModel: AuthenticationViewModel by viewModels()
    private val viewStateObserver = Observer<AuthenticationActionState> { state ->
        when (state) {
            is AuthenticationActionState.Authenticate -> launchAuthenticationEvent(state.event)
            is AuthenticationActionState.Failure -> reportErrorEvent(state.failure)
        }
    }
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObservers()
        initUi()
    }

    private fun initObservers() {
        viewModel.actionState.observe(this, viewStateObserver)
    }

    private fun initUi() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
     }

    // Login logic
    private val loginWithFirebaseAuthResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && FirebaseAuth.getInstance().currentUser != null) {
            viewModel.setCurrentUser(FirebaseAuth.getInstance().currentUser!!)
        }
        else {
            viewModel.reportOnAuthenticationError(IdpResponse.fromResultIntent(it.data))
        }
    }

    private fun launchAuthenticationEvent(event: Event<Long>) {
        event.getContentIfNotHandled()?.apply {
            if(FirebaseAuth.getInstance().currentUser != null) {
                Intent().apply {
                    intent.setClassName(
                        this@AuthenticationActivity.packageName,
                        PackageConstants.PACKAGE_INTENT_LOCATION_REMINDER
                    )
                    startActivity(intent)
                }
            }
            else {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
                )

                loginWithFirebaseAuthResult.launch(
                    AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        providers
                    ).build()
                )
            }
        }
    }

    private fun reportErrorEvent(failure: FailureModel) {
        Snackbar.make(findViewById(android.R.id.content), failure.messageForUser(this),
            Snackbar.LENGTH_LONG).show()
    }

}