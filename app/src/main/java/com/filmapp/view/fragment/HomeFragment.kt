package com.filmapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.filmapp.controller.FilmController
import com.filmapp.databinding.FragmentHomeBinding
import com.filmapp.model.Film
import com.filmapp.util.ThemeManager
import com.filmapp.view.AddEditActivity
import com.filmapp.view.BannerHeaderAdapter
import com.filmapp.view.DetailActivity
import com.filmapp.view.FilmAdapter
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val controller = FilmController()
    private lateinit var filmAdapter: FilmAdapter
    private lateinit var bannerAdapter: BannerHeaderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
        loadFilms()
    }

    override fun onResume() {
        super.onResume()
        loadFilms()
        bannerAdapter.startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        bannerAdapter.stopAutoScroll()
    }

    @OptIn(InternalSerializationApi::class)
    private fun setupRecyclerView() {
        bannerAdapter = BannerHeaderAdapter { film ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("film", film)
            startActivity(intent)
        }

        filmAdapter = FilmAdapter(
            mutableListOf(),
            onItemClick = { film ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("film", film)
                startActivity(intent)
            },
            onDeleteClick = { film -> confirmDelete(film) }
        )

        val concatAdapter = ConcatAdapter(bannerAdapter, filmAdapter)
        binding.rvFilms.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // Banner header selalu full width (span 2)
                    return if (position == 0 && bannerAdapter.itemCount > 0) 2 else 1
                }
            }
        }
        binding.rvFilms.adapter = concatAdapter
    }

    private fun setupButtons() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditActivity::class.java))
        }
        binding.btnTheme.setOnClickListener {
            ThemeManager.toggle(requireContext())
            requireActivity().recreate()
        }
    }

    @OptIn(InternalSerializationApi::class)
    private fun loadFilms() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvFilms.visibility = View.GONE
        binding.tvEmpty.visibility = View.GONE

        lifecycleScope.launch {
            controller.getAllFilms().fold(
                onSuccess = { films ->
                    binding.progressBar.visibility = View.GONE
                    if (films.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rvFilms.visibility = View.VISIBLE
                        bannerAdapter.updateFilms(films)
                        filmAdapter.updateData(films)
                    }
                },
                onFailure = {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Gagal memuat: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    @OptIn(InternalSerializationApi::class)
    private fun confirmDelete(film: Film) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Film")
            .setMessage("Hapus \"${film.judul}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    controller.deleteFilm(film.id).fold(
                        onSuccess = {
                            filmAdapter.removeItem(film)
                            Toast.makeText(requireContext(), "Dihapus", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAdapter.stopAutoScroll()
        _binding = null
    }
}
