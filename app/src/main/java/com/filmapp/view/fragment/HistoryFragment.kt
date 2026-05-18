package com.filmapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.filmapp.databinding.FragmentHistoryBinding
import com.filmapp.util.HistoryManager
import com.filmapp.view.DetailActivity
import com.filmapp.view.FilmAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FilmAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FilmAdapter(
            mutableListOf(),
            onItemClick = { film ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("film", film)
                startActivity(intent)
            },
            onDeleteClick = {}
        )
        binding.rvHistory.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
        binding.rvHistory.adapter = adapter

        binding.btnClear.setOnClickListener {
            HistoryManager.clear(requireContext())
            loadHistory()
        }

        loadHistory()
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    private fun loadHistory() {
        val history = HistoryManager.getAll(requireContext())
        adapter.updateData(history)
        binding.tvEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
        binding.rvHistory.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
        binding.btnClear.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
