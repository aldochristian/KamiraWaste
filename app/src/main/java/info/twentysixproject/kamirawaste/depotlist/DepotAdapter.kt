package info.twentysixproject.kamirawaste.depotlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.twentysixproject.kamirawaste.databinding.ItemDepotlistBinding

class DepotAdapter(var clickListener: DepotListener) : ListAdapter<Depots, DepotAdapter.ViewHolder>(DepotDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemDepotlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Depots, clickListener: DepotListener) {
            //binding.order = item
            //binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDepotlistBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DepotListener(val clickListener: (data: Depots) -> Unit) {
    fun onClick(depot: Depots) = clickListener(depot)
}

class DepotDiffCallback : DiffUtil.ItemCallback<Depots>() {

    override fun areItemsTheSame(oldItem: Depots, newItem: Depots): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Depots, newItem: Depots): Boolean {
        return oldItem == newItem
    }
}