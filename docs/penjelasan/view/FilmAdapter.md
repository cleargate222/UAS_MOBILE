# FilmAdapter.kt

## Letak File
`app/src/main/java/com/filmapp/view/FilmAdapter.kt`

## Tujuan
**Menghubungkan data film dengan tampilan grid kartu** di RecyclerView. Adapter bertugas mengambil setiap item dari list dan "memasangkan" datanya ke tampilan kartu yang sesuai.

---

## Hubungan dengan File Lain
- **Film.kt** → data yang ditampilkan di setiap kartu
- **item_film.xml** → layout satu kartu film
- **HomeFragment, SearchFragment, HistoryFragment** → membuat dan menggunakan adapter ini
- **Glide** → library untuk memuat gambar dari URL

---

## Konsep RecyclerView + Adapter
RecyclerView tidak tahu cara menampilkan data — itu tugas Adapter. Adapter seperti **pabrik kartu**: diberi data, menghasilkan tampilan kartu. RecyclerView hanya mengatur posisi dan scroll.

```
List<Film>  →  FilmAdapter  →  RecyclerView (tampilan grid)
```

---

## Penjelasan Kode

```kotlin
class FilmAdapter constructor(
    private var films: MutableList<Film>,
    private val onItemClick: (Film) -> Unit,
    private val onDeleteClick: (Film) -> Unit
)
```
Adapter menerima:
- `films` → data yang akan ditampilkan (`MutableList` karena bisa diubah)
- `onItemClick` → fungsi yang dipanggil saat kartu diklik (dikirim dari Fragment)
- `onDeleteClick` → fungsi yang dipanggil saat tombol hapus diklik

`(Film) -> Unit` adalah tipe **fungsi** di Kotlin — menerima `Film` sebagai parameter, tidak mengembalikan nilai.

---

```kotlin
inner class FilmViewHolder(val binding: ItemFilmBinding) :
    RecyclerView.ViewHolder(binding.root)
```
`ViewHolder` menyimpan referensi ke semua view dalam satu kartu. `inner class` berarti bisa mengakses properti dari kelas luar (`FilmAdapter`). Ini mencegah `findViewById` dipanggil berulang kali yang lambat.

---

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
    val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return FilmViewHolder(binding)
}
```
Dipanggil saat RecyclerView butuh kartu baru (saat scroll). Membuat tampilan kartu dari XML layout dan membungkusnya dalam ViewHolder.

---

```kotlin
override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
    val film = films[position]
    with(holder.binding) {
        tvTitle.text = film.judul
        ...
        root.setOnClickListener { onItemClick(film) }
        btnDelete.setOnClickListener { onDeleteClick(film) }
    }
}
```
Dipanggil saat kartu akan ditampilkan di layar. Mengisi data film ke view yang sesuai. `with(holder.binding) { }` memungkinkan akses langsung ke semua view tanpa prefix `holder.binding.`.

---

```kotlin
fun updateData(newFilms: List<Film>) {
    films.clear()
    films.addAll(newFilms)
    notifyDataSetChanged()
}
```
Mengganti seluruh data dan memberitahu RecyclerView untuk me-render ulang semua kartu. `notifyDataSetChanged()` adalah cara paling sederhana tapi kurang efisien untuk data besar.

---

```kotlin
fun removeItem(film: Film) {
    val index = films.indexOfFirst { it.id == film.id }
    if (index != -1) {
        films.removeAt(index)
        notifyItemRemoved(index)
    }
}
```
Menghapus satu item dengan animasi. `indexOfFirst { }` mencari posisi film berdasarkan ID. `notifyItemRemoved(index)` memberitahu RecyclerView untuk menghapus kartu di posisi tersebut dengan animasi, lebih efisien dari `notifyDataSetChanged()`.
