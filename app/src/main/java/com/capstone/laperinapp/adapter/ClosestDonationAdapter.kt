package com.capstone.laperinapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.databinding.ItemDonationBinding
import com.capstone.laperinapp.helper.meterToKilometer

class ClosestDonationAdapter() :
    PagingDataAdapter<DataItemDonation, ClosestDonationAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemDonation) {
            val distance = item.distance.toDouble()
            if (distance < 1000) {
                binding.tvItemDistance.text = "${item.distance} m"
            } else {
                val kilometer = meterToKilometer(distance)
                binding.tvItemDistance.text = kilometer
            }

            Glide.with(binding.root.context)
                .load(item.image)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivItemPhoto)

            binding.apply {
                tvItemName.text = item.name
                tvItemNamaPengirim.text = item.username
                tvItemCount.text = "Jumlah : ${item.total}"
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemDonation)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemDonation>() {
            override fun areItemsTheSame(
                oldItem: DataItemDonation,
                newItem: DataItemDonation
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: DataItemDonation,
                newItem: DataItemDonation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}