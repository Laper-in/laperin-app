package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.BookmarksItem
import com.capstone.laperinapp.databinding.ItemBookmarkProfileBinding

class BookmarkProfileAdapter: PagingDataAdapter<BookmarksItem, BookmarkProfileAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder( private val binding: ItemBookmarkProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarksItem){
            binding.apply {

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
        fun onItemClicked(data: BookmarksItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarksItem>() {
            override fun areItemsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}