package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.databinding.ItemBookmarkProfileBinding
import com.capstone.laperinapp.helper.formatDurationList

class BookmarkProfileAdapter: PagingDataAdapter<DataItemBookmark, BookmarkProfileAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder( private val binding: ItemBookmarkProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemBookmark){
            Glide.with(itemView.context)
                .load(item.recipe.image)
                .transform(CenterCrop(), RoundedCorners(15))
                .into(binding.imgItem)
            val time = item.recipe.time
            val formatedTime = formatDurationList(binding.root.context, time)
            binding.apply {
                tvItemName.text = item.recipe.name
                tvItemDescription.text = item.recipe.description
                tvDurasi.text = formatedTime
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

    interface OnItemClickCallback{
        fun onItemClicked(data: DataItemBookmark)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemBookmark>() {
            override fun areItemsTheSame(oldItem: DataItemBookmark, newItem: DataItemBookmark): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItemBookmark, newItem: DataItemBookmark): Boolean {
                return oldItem == newItem
            }
        }
    }
}