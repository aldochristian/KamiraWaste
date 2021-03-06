package info.twentysixproject.kamirawaste.orderpool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.twentysixproject.kamirawaste.databinding.ItemDepoOrderpoolBinding
import info.twentysixproject.kamirawaste.orderpool.Orders

class DepoOrderpoolAdapter(var clickListener: DepoOrderpoolListener) : ListAdapter<Orders, DepoOrderpoolAdapter.ViewHolder>(DepoOrderpoolDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemDepoOrderpoolBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Orders, clickListener: DepoOrderpoolListener) {
            binding.order = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDepoOrderpoolBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DepoOrderpoolListener(val clickListener: (data: Orders) -> Unit) {
    fun onClick(order: Orders) = clickListener(order)
}

class DepoOrderpoolDiffCallback : DiffUtil.ItemCallback<Orders>() {

    override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem == newItem
    }
}