package com.capstone.laperinapp.data.favorite.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.favorite.entity.Favorite
import com.capstone.laperinapp.databinding.ItemCategoryBinding
import com.capstone.laperinapp.databinding.ItemRecipesPopularBinding
import com.capstone.laperinapp.databinding.ItemRecipesRekomendasiBinding
import com.capstone.laperinapp.ui.detail.DetailActivity

class FavoriteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_REKOMENDASI = 1
    private val TYPE_POPULAR = 2
    private val listFavorite = ArrayList<Favorite>()
    fun setListFavorit(favorite: List<Favorite>) {
        listFavorite.clear()
        listFavorite.addAll(favorite)
        notifyDataSetChanged()
    }

    inner class FavoriteRekomendasiViewHolder(val binding: ItemRecipesRekomendasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(favorite: Favorite) {
                with(binding) {
                    tvItemName.text = favorite.name
                    Glide.with(itemView)
                        .load(favorite.image)
                        .centerCrop()
                        .into(imgItemPhoto)
                    tvItemCategory.text = favorite.category
                    cvItemRekomendasi.setOnClickListener{
                        val intent = Intent(it.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_DATA, favorite.name)
                        it.context.startActivity(intent)
                    }
                }
            }
        }

    inner class FavoritePopularViewHolder(val binding: ItemRecipesPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(favorite: Favorite) {
                with(binding) {
                    tvItemName.text = favorite.name
                    Glide.with(itemView)
                        .load(favorite.image)
                        .centerCrop()
                        .into(imgItemPhoto)
                    tvItemCategory.text= favorite.category
                    cvItemPopular.setOnClickListener {
                        val intent = Intent(it.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_DATA, favorite.name)
                        it.context.startActivity(intent)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_REKOMENDASI -> {
                val binding = ItemRecipesRekomendasiBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FavoriteRekomendasiViewHolder(binding)
            }
            TYPE_POPULAR -> {
                val binding = ItemRecipesPopularBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FavoritePopularViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_REKOMENDASI -> {
                (holder as FavoriteRekomendasiViewHolder).bind(listFavorite[position])
            }
            TYPE_POPULAR -> {
                (holder as FavoritePopularViewHolder).bind(listFavorite[position])
            }
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun getItemViewType(position: Int): Int {
        return when {
            position < listFavorite.size && listFavorite[position].name.toInt() == TYPE_REKOMENDASI -> TYPE_REKOMENDASI
            position < listFavorite.size && listFavorite[position].name.toInt() == TYPE_POPULAR -> TYPE_POPULAR
            else -> throw IllegalArgumentException("Invalid position or item type")
        }
    }
}
