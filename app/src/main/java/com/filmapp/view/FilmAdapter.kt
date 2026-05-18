package com.filmapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filmapp.R
import com.filmapp.databinding.ItemFilmBinding
import com.filmapp.model.Film
import kotlinx.serialization.InternalSerializationApi

class FilmAdapter @OptIn(InternalSerializationApi::class) constructor(
    private var films: MutableList<Film>,
    private val onItemClick: (Film) -> Unit,
    private val onDeleteClick: (Film) -> Unit
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    @OptIn(InternalSerializationApi::class)
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        with(holder.binding) {
            tvTitle.text = film.judul
            tvGenre.text = film.kategori
            tvYear.text = film.tahunRilis
            tvRating.text = film.ratingDisplay

            Glide.with(root.context)
                .load(film.gambarPoster)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .centerCrop()
                .into(ivPoster)

            root.setOnClickListener { onItemClick(film) }
            btnDelete.visibility = android.view.View.VISIBLE
            btnDelete.setOnClickListener { onDeleteClick(film) }
        }
    }

    @OptIn(InternalSerializationApi::class)
    override fun getItemCount() = films.size

    @OptIn(InternalSerializationApi::class)
    fun updateData(newFilms: List<Film>) {
        films.clear()
        films.addAll(newFilms)
        notifyDataSetChanged()
    }

    @OptIn(InternalSerializationApi::class)
    fun removeItem(film: Film) {
        val index = films.indexOfFirst { it.id == film.id }
        if (index != -1) {
            films.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
