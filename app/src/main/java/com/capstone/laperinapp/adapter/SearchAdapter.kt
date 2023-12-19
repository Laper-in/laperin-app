package com.capstone.laperinapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.response.DataItemIngredient
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.databinding.ItemSearchBinding

class SearchAdapter :
    PagingDataAdapter<DataItemIngredient, SearchAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemIngredient) {
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.imgItem)
            val name = item.name
            val capitalized = name.substring(0, 1).toUpperCase() + name.substring(1)
            binding.apply {
                tvName.text = capitalized
            }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            onItemClickCallback.onItemClicked(item, holder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemIngredient, holder: MyViewHolder)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemIngredient>() {
            override fun areItemsTheSame(
                oldItem: DataItemIngredient,
                newItem: DataItemIngredient
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: DataItemIngredient,
                newItem: DataItemIngredient
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}