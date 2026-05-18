package com.filmapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.filmapp.controller.FilmController
import com.filmapp.databinding.FragmentSearchBinding
import com.filmapp.model.Film
import com.filmapp.view.DetailActivity
import com.filmapp.view.FilmAdapter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val controller = FilmController()
    private lateinit var adapter: FilmAdapter
    private var allFilms: List<Film> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
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
            onDeleteClick = {}
        )
        binding.rvSearch.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
        binding.rvSearch.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = if (query.isEmpty()) allFilms
                else allFilms.filter {
                    it.judul.lowercase().contains(query) ||
                    it.kategori.lowercase().contains(query) ||
                    it.ringkasan.lowercase().contains(query)
                }
                adapter.updateData(filtered)
                binding.tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
                binding.rvSearch.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun loadFilms() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            controller.getAllFilms().fold(
                onSuccess = { films ->
                    allFilms = films
                    binding.progressBar.visibility = View.GONE
                    adapter.updateData(films)
                    binding.rvSearch.visibility = View.VISIBLE
                },
                onFailure = {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Gagal memuat", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
