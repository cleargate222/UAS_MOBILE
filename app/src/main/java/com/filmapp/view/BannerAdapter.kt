package com.filmapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filmapp.R
import com.filmapp.databinding.ItemBannerBinding
import com.filmapp.model.Film
import kotlinx.serialization.InternalSerializationApi

class BannerAdapter @OptIn(InternalSerializationApi::class) constructor(
    private val films: List<Film>,
    private val onClick: (Film) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    @OptIn(InternalSerializationApi::class)
    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val film = films[position]
        with(holder.binding) {
            tvBannerTitle.text = film.judul
            tvBannerGenre.text = film.kategori.uppercase()
            tvBannerRating.text = film.ratingDisplay

            Glide.with(root.context)
                .load(film.gambarSampul.ifEmpty { film.gambarPoster })
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .centerCrop()
                .into(ivBanner)

            root.setOnClickListener { onClick(film) }
        }
    }

    @OptIn(InternalSerializationApi::class)
    override fun getItemCount() = films.size
}
