package paolo.udacity.location_reminder.features.views.common.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.databinding.ActivityLocationReminderBinding


@AndroidEntryPoint
class LocationReminderActivity:
    AppCompatActivity() {

    private lateinit var binding: ActivityLocationReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_reminder)
    }

}