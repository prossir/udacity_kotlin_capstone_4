package paolo.udacity.location_reminder.features.views.showRemindersOnMap.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.features.views.common.views.LocationReminderViewModel


class ChooseMapStyleDialogFragment: DialogFragment() {

    private val viewModel by viewModels<LocationReminderViewModel>({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Map Style Options")
                .setItems(R.array.map_styles_array) { dialog, which ->
                    // The 'which' argument contains the index position of the selected item
                    viewModel.setMapStyle(which)
                    dialog.dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}