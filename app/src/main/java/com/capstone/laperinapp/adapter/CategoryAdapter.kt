package com.capstone.laperinapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.laperinapp.data.model.Category
import com.capstone.laperinapp.databinding.ItemCategoryBinding

class CategoryAdapter(private val listCategory: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category){
            binding.apply {
                tvItemName.text = item.name
                imgItemPhoto.setImageResource(item.image)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(item)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Category)
    }

}