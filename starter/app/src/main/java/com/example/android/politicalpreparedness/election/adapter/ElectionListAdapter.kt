package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }
    }
}

// Create ElectionViewHolder
class ElectionViewHolder(private val binding: ElectionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ElectionViewHolder(ElectionItemBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(election: Election) {
        binding.election = election
        binding.executePendingBindings()
    }
}

// Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

// Create ElectionListener
class ElectionListener(val clickListener: (Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}