package paolo.udacity.auth.platform.views.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import paolo.udacity.auth.databinding.FragmentWelcomeBinding
import paolo.udacity.auth.platform.views.common.views.AuthenticationViewModel

class WelcomeFragment: Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWelcomeBinding.inflate(inflater)
        initUi()
        return binding.root
    }

    private fun initUi() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

}