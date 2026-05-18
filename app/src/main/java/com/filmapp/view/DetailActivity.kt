package com.filmapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.filmapp.R
import com.filmapp.databinding.ActivityDetailBinding
import com.filmapp.model.Film
import com.filmapp.util.HistoryManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val film = intent.getParcelableExtra<Film>("film") ?: return

        setupToolbar()
        bindFilm(film)
        HistoryManager.add(this, film)

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("film", film)
            startActivity(intent)
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun bindFilm(film: Film) {
        supportActionBar?.title = film.judul

        Glide.with(this)
            .load(film.gambarSampul.ifEmpty { film.gambarPoster })
            .placeholder(R.drawable.ic_movie_placeholder)
            .error(R.drawable.ic_movie_placeholder)
            .centerCrop()
            .into(binding.ivPoster)

        binding.tvTitle.text = film.judul
        binding.tvGenre.text = film.kategori
        binding.tvYear.text = film.tahunRilis
        binding.tvRating.text = film.ratingDisplay
        binding.tvDirector.text = if (film.urlTrailer.isNotEmpty()) "🎬 Trailer tersedia" else ""
        binding.tvDescription.text = film.ringkasan
    }
}
