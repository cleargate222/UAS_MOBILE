package com.filmapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.filmapp.controller.FilmController
import com.filmapp.databinding.ActivityAddEditBinding
import com.filmapp.model.Film
import kotlinx.coroutines.launch

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private val controller = FilmController()
    private var existingFilm: Film? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        existingFilm = intent.getParcelableExtra("film")

        setupToolbar()
        populateFields()
        setupSaveButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = if (existingFilm != null) "Edit Film" else "Tambah Film"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun populateFields() {
        existingFilm?.let { film ->
            binding.etTitle.setText(film.judul)
            binding.etGenre.setText(film.kategori)
            binding.etYear.setText(film.tahunRilis)
            binding.etRating.setText(film.skorRating.toString())
            binding.etPosterUrl.setText(film.gambarPoster)
            binding.etTrailerUrl.setText(film.urlTrailer)
            binding.etDescription.setText(film.ringkasan)
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val judul = binding.etTitle.text.toString().trim()
            val kategori = binding.etGenre.text.toString().trim()
            val rating = binding.etRating.text.toString().trim().toIntOrNull() ?: 0
            val posterUrl = binding.etPosterUrl.text.toString().trim()
            val trailerUrl = binding.etTrailerUrl.text.toString().trim()
            val ringkasan = binding.etDescription.text.toString().trim()

            if (judul.isEmpty() || kategori.isEmpty()) {
                Toast.makeText(this, "Judul dan kategori wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val yearInput = binding.etYear.text.toString().trim().toIntOrNull() ?: 2026
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.YEAR, yearInput)
            calendar.set(java.util.Calendar.MONTH, 0)
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val releaseTimestamp = calendar.timeInMillis / 1000

            val film = Film(
                judul = judul,
                kategori = kategori,
                skorRating = rating,
                gambarPoster = posterUrl,
                gambarSampul = posterUrl,
                urlTrailer = trailerUrl,
                ringkasan = ringkasan,
                tanggalRilis = releaseTimestamp
            )

            binding.progressBar.visibility = View.VISIBLE
            binding.btnSave.isEnabled = false

            lifecycleScope.launch {
                val result = if (existingFilm != null) {
                    controller.updateFilm(existingFilm!!.id, film)
                } else {
                    controller.createFilm(film)
                }

                result.fold(
                    onSuccess = {
                        val msg = if (existingFilm != null) "Film diperbarui" else "Film ditambahkan"
                        Toast.makeText(this@AddEditActivity, msg, Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSave.isEnabled = true
                        Toast.makeText(this@AddEditActivity, "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
