package info.twentysixproject.kamirawaste.orderlist.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import info.twentysixproject.kamirawaste.orderlist.Orders
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import info.twentysixproject.kamirawaste.databinding.ItemOrderlistBinding

class OrderAdapter(var clickListener: OrderListener) : ListAdapter<Orders, OrderAdapter.ViewHolder>(OrderDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemOrderlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Orders, clickListener: OrderListener) {
            binding.order = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOrderlistBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OrderListener(val clickListener: (data: Orders) -> Unit) {
    fun onClick(order: Orders) = clickListener(order)
}

class OrderDiffCallback : DiffUtil.ItemCallback<Orders>() {

    override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem == newItem
    }
}