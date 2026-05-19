# HistoryFragment.kt

## Letak File
`app/src/main/java/com/filmapp/view/fragment/HistoryFragment.kt`

## Tujuan
Menampilkan **daftar film yang pernah dibuka** oleh pengguna. Data diambil dari penyimpanan lokal (SharedPreferences), bukan dari API — sehingga bisa dilihat meski offline.

---

## Hubungan dengan File Lain
- **HistoryManager.kt** → sumber data riwayat (baca dan hapus)
- **FilmAdapter.kt** → menampilkan riwayat dalam grid (adapter yang sama dengan Home dan Search)
- **DetailActivity.kt** → dibuka saat kartu film diklik
- **fragment_history.xml** → layout halaman ini

---

## Penjelasan Kode

```kotlin
adapter = FilmAdapter(
    mutableListOf(),
    onItemClick = { film ->
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("film", film)
        startActivity(intent)
    },
    onDeleteClick = {}  // ← kosong, tidak ada fungsi hapus di riwayat
)
```
`FilmAdapter` digunakan kembali di sini. `onDeleteClick = {}` adalah lambda kosong — tombol hapus di kartu tidak melakukan apa-apa di halaman riwayat (karena riwayat tidak perlu hapus per item, hanya "Hapus Semua").

---

```kotlin
binding.btnClear.setOnClickListener {
    HistoryManager.clear(requireContext())
    loadHistory()
}
```
Tombol "Hapus Semua" memanggil `HistoryManager.clear()` lalu reload tampilan. `loadHistory()` dipanggil ulang agar tampilan diperbarui (menampilkan state kosong).

---

```kotlin
override fun onResume() {
    super.onResume()
    loadHistory()
}
```
`loadHistory()` dipanggil di `onResume` agar riwayat selalu diperbarui saat pengguna kembali ke tab ini. Misalnya: pengguna buka film dari tab Home → kembali ke tab History → riwayat langsung menampilkan film yang baru dibuka.

---

```kotlin
private fun loadHistory() {
    val history = HistoryManager.getAll(requireContext())
    adapter.updateData(history)
    binding.tvEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
    binding.rvHistory.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
    binding.btnClear.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
}
```
Mengambil data dari `HistoryManager` dan mengatur visibilitas elemen:
- Jika riwayat kosong → tampilkan pesan kosong, sembunyikan list dan tombol hapus
- Jika ada riwayat → tampilkan list dan tombol hapus, sembunyikan pesan kosong

`View.VISIBLE` = tampil, `View.GONE` = disembunyikan dan tidak mengambil ruang layout.
