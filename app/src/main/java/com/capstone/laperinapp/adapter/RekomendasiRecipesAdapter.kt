package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ItemRecipesRekomendasiBinding

class RekomendasiRecipesAdapter() :
    PagingDataAdapter<DataItemRecipes, RekomendasiRecipesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemRecipesRekomendasiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemRecipes){
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.imgItemPhoto)
            binding.apply {
                tvItemName.text = item.name
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesRekomendasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    interface OnItemClickCallback{
        fun onItemClicked(data: DataItemRecipes)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemRecipes>() {
            override fun areItemsTheSame(oldItem: DataItemRecipes, newItem: DataItemRecipes): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItemRecipes, newItem: DataItemRecipes): Boolean {
                return oldItem == newItem
            }
        }
    }
}