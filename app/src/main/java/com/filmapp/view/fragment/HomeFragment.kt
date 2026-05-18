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
import com.filmapp.controller.FilmController
import com.filmapp.databinding.FragmentHomeBinding
import com.filmapp.model.Film
import com.filmapp.util.ThemeManager
import com.filmapp.view.AddEditActivity
import com.filmapp.view.DetailActivity
import com.filmapp.view.FilmAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val controller = FilmController()
    private lateinit var adapter: FilmAdapter

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
    }

    private fun setupRecyclerView() {
        adapter = FilmAdapter(
            mutableListOf(),
            onItemClick = { film ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("film", film)
                startActivity(intent)
            },
            onDeleteClick = { film -> confirmDelete(film) }
        )
        binding.rvFilms.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
        binding.rvFilms.adapter = adapter
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
                        adapter.updateData(films)
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

    private fun confirmDelete(film: Film) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Film")
            .setMessage("Hapus \"${film.judul}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    controller.deleteFilm(film.id).fold(
                        onSuccess = {
                            adapter.removeItem(film)
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
        _binding = null
    }
}
