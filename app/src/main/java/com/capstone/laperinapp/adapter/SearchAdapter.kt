package com.capstone.laperinapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.databinding.ItemSearchBinding

class SearchAdapter :
    PagingDataAdapter<IngredientItem, SearchAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IngredientItem) {
            binding.apply {
                tvName.text = item.name
            }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item, holder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: IngredientItem, holder: MyViewHolder)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IngredientItem>() {
            override fun areItemsTheSame(
                oldItem: IngredientItem,
                newItem: IngredientItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: IngredientItem,
                newItem: IngredientItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}