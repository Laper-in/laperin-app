package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.databinding.ItemDonasiDoneBinding

class CompletedDonationAdapter :
    PagingDataAdapter<DataItemDonation, CompletedDonationAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemDonasiDoneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemDonation) {
            Glide.with(binding.root.context)
                .load(item.image)
                .transform(CenterCrop(), RoundedCorners(15))
                .into(binding.imgItem)
            binding.apply {
                tvItemName.text = item.name
                tvItemJenis.text = item.category
                tvItemJumlah.text = item.total.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item, holder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDonasiDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemDonation, holder: ViewHolder)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemDonation>() {
            override fun areItemsTheSame(
                oldItem: DataItemDonation,
                newItem: DataItemDonation
            ): Boolean {
                return oldItem.idDonation == newItem.idDonation
            }

            override fun areContentsTheSame(
                oldItem: DataItemDonation,
                newItem: DataItemDonation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}