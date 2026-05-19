# SearchFragment.kt

## Letak File
`app/src/main/java/com/filmapp/view/fragment/SearchFragment.kt`

## Tujuan
Halaman pencarian film. Menampilkan semua film saat pertama dibuka, lalu **memfilter secara real-time** saat pengguna mengetik di kolom pencarian — tanpa perlu menekan tombol cari.

---

## Hubungan dengan File Lain
- **FilmController.kt** → mengambil semua film dari API saat halaman dibuka
- **FilmAdapter.kt** → menampilkan hasil pencarian dalam grid
- **DetailActivity.kt** → dibuka saat kartu film diklik
- **fragment_search.xml** → layout halaman ini

---

## Penjelasan Kode

```kotlin
private var allFilms: List<Film> = emptyList()
```
Menyimpan **semua film** yang diambil dari API. Saat pengguna mengetik, filter dilakukan dari list ini — bukan memanggil API lagi setiap ketikan. Ini membuat pencarian terasa instan.

---

```kotlin
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
    }
    override fun beforeTextChanged(...) {}
    override fun onTextChanged(...) {}
})
```
`TextWatcher` memantau perubahan teks secara real-time. `afterTextChanged` dipanggil setiap kali teks berubah.

Logika filter:
- `.trim()` → hapus spasi di awal/akhir
- `.lowercase()` → ubah ke huruf kecil agar pencarian tidak case-sensitive
- `.filter { }` → kembalikan hanya item yang memenuhi kondisi
- `.contains(query)` → cek apakah teks mengandung kata kunci
- `||` → OR — cukup salah satu kondisi terpenuhi

`object : TextWatcher { }` adalah **anonymous class** — implementasi interface tanpa membuat kelas terpisah. `beforeTextChanged` dan `onTextChanged` harus ada karena interface `TextWatcher` mewajibkannya, tapi tidak perlu diisi.

---

```kotlin
onDeleteClick = { film ->
    lifecycleScope.launch {
        controller.deleteFilm(film.id).fold(
            onSuccess = {
                adapter.removeItem(film)
                allFilms = allFilms.filter { it.id != film.id }
            },
            ...
        )
    }
}
```
Setelah hapus berhasil, `allFilms` juga diperbarui. Ini penting agar jika pengguna mengetik ulang, film yang sudah dihapus tidak muncul lagi di hasil filter.

`allFilms.filter { it.id != film.id }` membuat list baru yang tidak mengandung film yang dihapus. Di Kotlin, `List` bersifat immutable — tidak bisa diubah langsung, harus dibuat list baru.
