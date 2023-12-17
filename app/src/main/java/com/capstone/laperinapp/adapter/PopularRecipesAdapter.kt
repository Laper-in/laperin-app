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
import com.capstone.laperinapp.data.response.RecipeItem
import com.capstone.laperinapp.databinding.ItemBookmarkProfileBinding
import com.capstone.laperinapp.databinding.ItemRecipesPopularBinding

class PopularRecipesAdapter() : PagingDataAdapter<RecipeItem, PopularRecipesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookmarkProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(val binding: ItemBookmarkProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeItem){
            Glide.with(binding.root.context)
                .load(item.image)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(binding.imgItem)
            binding.apply {
                tvItemName.text = item.name
                tvItemDescription.text = item.category
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