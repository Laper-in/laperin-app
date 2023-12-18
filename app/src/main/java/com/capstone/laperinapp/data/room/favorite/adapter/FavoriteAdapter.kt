package com.capstone.laperinapp.data.room.favorite.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.room.favorite.entity.Favorite
import com.capstone.laperinapp.databinding.ItemCategoryBinding
import com.capstone.laperinapp.databinding.ItemRecipesPopularBinding
import com.capstone.laperinapp.databinding.ItemRecipesRekomendasiBinding
import com.capstone.laperinapp.ui.detail.DetailActivity

class FavoriteAdapter : ListAdapter<Favorite, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemRecipesPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Favorite){
            binding.apply {
                tvItemName.text = item.name
                Glide.with(itemView.context)
                    .load(item.image)
                    .into(imgItemPhoto)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecipesPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(item)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Favorite)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }

}
