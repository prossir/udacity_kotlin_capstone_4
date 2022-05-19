package paolo.udacity.testing

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario


fun View.getBinding(): ViewDataBinding? = DataBindingUtil.getBinding(this)

/**
 * Sets the activity from an [ActivityScenario] to be used from [DataBindingIdlingResource].
 */
fun DataBindingIdlingResource.monitorActivity(
    activityScenario: ActivityScenario<out FragmentActivity>
) {
    activityScenario.onActivity {
        this.dataBindingActivity = it
    }
}

/**
 * Sets the fragment from a [FragmentScenario] to be used from [DataBindingIdlingResource].
 */
fun DataBindingIdlingResource.monitorFragment(fragmentScenario: FragmentScenario<Fragment>) {
    // out removed from <Fragment as the reified type was conflicting with the reflection>
    fragmentScenario.onFragment {
        dataBindingActivity = it.requireActivity()
    }
}