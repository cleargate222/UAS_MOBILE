package com.filmapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.filmapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileData()
        setupLogoutButton()
    }

    private fun setupProfileData() {
        // Data default profil untuk Brahmana sesuai request tugas
        val name = "Brahmana"
        val email = "brahmana@mail.com"

        binding.tvProfileName.text = name
        binding.tvProfileEmail.text = email
        
        // Ambil inisial nama untuk avatar bulat (misal: "Brahmana" -> "B")
        binding.tvAvatarInitials.text = if (name.isNotEmpty()) {
            name.substring(0, Math.min(name.length, 2)).uppercase()
        } else {
            "U"
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya, Keluar") { _, _ ->
                    Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                    // Menutup aplikasi secara bersih dan menyeluruh
                    requireActivity().finishAffinity()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
