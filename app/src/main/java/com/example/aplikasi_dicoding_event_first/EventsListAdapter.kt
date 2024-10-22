package com.example.aplikasi_dicoding_event_first

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.EventCardListBinding

class EventsListAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<ListEventsItem, EventsListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(
        private val binding: EventCardListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            Glide.with(context)
                .load(event.imageLogo)
                .into(binding.ivEvent)

            binding.tvTitle.text = event.name
            binding.tvSummary.text = event.summary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val binding = EventCardListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener{
            onClick(event.id)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
