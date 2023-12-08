package com.capstone.laperinapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.RecipeItem
import com.capstone.laperinapp.databinding.ItemRecipesPopularBinding

class PopularRecipesAdapter() : PagingDataAdapter<RecipeItem, PopularRecipesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    class ViewHolder(val binding: ItemRecipesPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeItem){
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.imgItemPhoto)
            binding.apply {
                tvItemName.text = item.name
                tvItemCategory.text = item.category
            }
        }

    }

    interface OnItemClickCallback{
        fun onItemClicked(data: RecipeItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeItem>() {
            override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}