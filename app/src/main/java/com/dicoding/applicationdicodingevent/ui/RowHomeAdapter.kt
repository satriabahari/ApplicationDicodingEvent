package com.dicoding.applicationdicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.applicationdicodingevent.data.response.ListEventsItem
import com.dicoding.applicationdicodingevent.databinding.ItemRowHomeBinding

class RowHomeAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, RowHomeAdapter.MyViewHolder>(EventAdapter.DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemRowHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvItemName.text = event.name
            binding.tvItemSummary.text = event.summary
            Glide.with(binding.imgItemPhoto.context)
                .load(event.mediaCover)
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }
}