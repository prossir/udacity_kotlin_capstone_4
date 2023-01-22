package paolo.udacity.location_reminder.features.views.showRemindersOnMap.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import paolo.udacity.location_reminder.databinding.ItemReminderBinding
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel
import paolo.udacity.location_reminder.features.views.showRemindersOnMap.interfaces.OnReminderClickedListener


class RemindersAdapter(
    private val listener: OnReminderClickedListener
): ListAdapter<ReminderModel, RemindersAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }

    class ViewHolder(private val binding: ItemReminderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: ReminderModel, listener: OnReminderClickedListener) {
            binding.reminder = reminder
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemReminderBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val diffUtil = object :
            DiffUtil.ItemCallback<ReminderModel>() {
            override fun areItemsTheSame(old: ReminderModel, new: ReminderModel) =
                false
            override fun areContentsTheSame(old: ReminderModel, new: ReminderModel) =
                false
        }
    }

}