package com.example.aplikasi_dicoding_event_first

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasi_dicoding_event_first.databinding.EventCardListBinding
import com.example.aplikasi_dicoding_event_first.utils.data.IEvent

// Erm, generics because I'm just curious xD
class EventsFavoriteAdapter<T : IEvent>(
    private val onClick: (Int) -> Unit
) : ListAdapter<T, EventsFavoriteAdapter.MyViewHolder<T>>(createDiffCallback()) {

    class MyViewHolder<T : IEvent>(
        private val binding: EventCardListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: T) {
            Glide.with(context)
                .load(event.imageLogo)
                .into(binding.ivEvent)

            binding.tvTitle.text = event.name
            binding.tvSummary.text = event.summary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<T> {
        val context = parent.context
        val binding = EventCardListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T>, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onClick(event.id)
        }
    }

    companion object {
        private fun <T : IEvent> createDiffCallback(): DiffUtil.ItemCallback<T> {
            return object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}
