package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.RecipesItem
import com.capstone.laperinapp.databinding.ItemRecipesPopularBinding

class SearchRecipesAdapter: PagingDataAdapter<RecipesItem, SearchRecipesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemRecipesPopularBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipesItem: RecipesItem) {
            with(binding) {
                tvItemName.text = recipesItem.name
                tvItemCategory.text = recipesItem.category
                Glide.with(itemView.context)
                    .load(recipesItem.image)
                    .into(imgItemPhoto)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipesItem = getItem(position)
        if (recipesItem != null) {
            holder.bind(recipesItem)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(recipesItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecipesPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RecipesItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipesItem>() {
            override fun areItemsTheSame(oldItem: RecipesItem, newItem: RecipesItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecipesItem, newItem: RecipesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}