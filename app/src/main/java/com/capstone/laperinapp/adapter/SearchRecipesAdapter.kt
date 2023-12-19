package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ItemBookmarkProfileBinding
import com.capstone.laperinapp.helper.formatDurationList

class SearchRecipesAdapter: PagingDataAdapter<DataItemRecipes, SearchRecipesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemBookmarkProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemRecipes){
            Glide.with(binding.root.context)
                .load(item.image)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(binding.imgItem)
            val formatedDuration = formatDurationList(binding.root.context, item.time)
            binding.apply {
                tvItemName.text = item.name
                tvDurasi.text = formatedDuration
                tvItemDescription.text = item.description
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookmarkProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemRecipes)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemRecipes>() {
            override fun areItemsTheSame(
                oldItem: DataItemRecipes,
                newItem: DataItemRecipes
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataItemRecipes,
                newItem: DataItemRecipes
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}