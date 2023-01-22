package paolo.udacity.location_reminder.features.views.maintainReminder.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.databinding.FragmentMaintainReminderBinding
import paolo.udacity.location_reminder.features.views.common.views.LocationReminderViewModel


class MaintainReminderFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<LocationReminderViewModel>( { requireParentFragment() } )
    private lateinit var binding: FragmentMaintainReminderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_maintain_reminder, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    companion object {
        fun newInstance() = MaintainReminderFragment()
    }

}