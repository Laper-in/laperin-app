package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.databinding.ItemDonationBinding
import com.capstone.laperinapp.helper.meterToKilometer

class DonationAdapter() : PagingDataAdapter<DonationsItem, DonationAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private val binding: ItemDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DonationsItem) {
            val distance = item.distance
            if (distance < 1000) {
                binding.tvItemDistance.text = "${item.distance} m"
            } else {
                val kilometer = meterToKilometer(distance)
                binding.tvItemDistance.text = kilometer
            }

            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivItemPhoto)
            binding.apply {
                tvItemName.text = item.name
                tvItemCount.text = "Jumlah : ${item.total}"
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                if (::onItemClickCallback.isInitialized) {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DonationsItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DonationsItem>() {
            override fun areItemsTheSame(oldItem: DonationsItem, newItem: DonationsItem): Boolean {
                return oldItem.idDonation == newItem.idDonation
            }

            override fun areContentsTheSame(oldItem: DonationsItem, newItem: DonationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}