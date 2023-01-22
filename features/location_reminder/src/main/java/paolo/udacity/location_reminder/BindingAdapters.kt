package paolo.udacity.location_reminder

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    if(isVisible) {
        view.visibility = View.VISIBLE
    }
    else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("setAdapter")
fun setAdapter(recyclerview: RecyclerView, adapter: ListAdapter<Any, RecyclerView.ViewHolder>?) {
    adapter?.let {
        recyclerview.adapter = it
    }
}