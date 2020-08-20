package com.example.android.marsrealestate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class PhotoGridAdapter(val clickListener: OnClickListener) :ListAdapter<MarsProperty,PhotoGridAdapter.MarsPropertyViewHolder>(DiffCallback){
    companion object DiffCallback:DiffUtil.ItemCallback<MarsProperty>(){
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {

            return  oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class MarsPropertyViewHolder(private  var binding:GridViewItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: MarsProperty){
            binding.property=marsProperty
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPropertyViewHolder {
        return MarsPropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty=getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(marsProperty)
        }
        holder.bind(marsProperty)
    }

}


class OnClickListener(val clickListener:(marsproperty:MarsProperty)->Unit){
    fun onClick(marsProperty: MarsProperty)=clickListener(marsProperty)
}