package com.capstone.laperinapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.databinding.ItemSearchBinding

class SearchAdapter :
    PagingDataAdapter<IngredientsItem, SearchAdapter.MyViewHolder>(DIFF_CALLBACK), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var ingredients: PagingData<IngredientsItem>? = null
    private var ingredientsFiltered: PagingData<IngredientsItem>? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IngredientsItem) {
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
        fun onItemClicked(data: IngredientsItem, holder: MyViewHolder)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint.isNullOrEmpty()){
                    ingredientsFiltered = ingredients
                } else {
                    ingredientsFiltered = ingredients?.filter {
                        it.name.contains(constraint, true)
                    }
                }

                val result = FilterResults()
                result.values = ingredientsFiltered
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ingredientsFiltered = results?.values as PagingData<IngredientsItem>?
                notifyDataSetChanged()
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IngredientsItem>() {
            override fun areItemsTheSame(
                oldItem: IngredientsItem,
                newItem: IngredientsItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: IngredientsItem,
                newItem: IngredientsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}