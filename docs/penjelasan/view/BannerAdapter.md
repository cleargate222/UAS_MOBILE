# BannerAdapter.kt

## Letak File
`app/src/main/java/com/filmapp/view/BannerAdapter.kt`

## Tujuan
Adapter khusus untuk **ViewPager2 di carousel banner**. Menampilkan gambar sampul film beserta judul, genre, dan rating di atas gambar.

---

## Hubungan dengan File Lain
- **BannerHeaderAdapter.kt** → membuat dan menggunakan `BannerAdapter` di dalam ViewPager2
- **Film.kt** → data yang ditampilkan di setiap slide banner
- **item_banner.xml** → layout satu slide banner

---

## Perbedaan dengan FilmAdapter

| | FilmAdapter | BannerAdapter |
|--|--|--|
| Digunakan di | RecyclerView (grid) | ViewPager2 (carousel) |
| Layout | `item_film.xml` | `item_banner.xml` |
| Fungsi hapus | Ada | Tidak ada |
| Gambar | `gambarPoster` | `gambarSampul` (fallback ke poster) |

---

## Penjelasan Kode

```kotlin
class BannerAdapter constructor(
    private val films: List<Film>,
    private val onClick: (Film) -> Unit
)
```
Menerima list film (tidak mutable karena banner tidak perlu diedit) dan fungsi callback saat banner diklik.

---

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
    val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return BannerViewHolder(binding)
}
```
Membuat tampilan satu slide dari `item_banner.xml`. Sama seperti `FilmAdapter` tapi menggunakan binding yang berbeda.

---

```kotlin
tvBannerGenre.text = film.kategori.uppercase()
```
`.uppercase()` mengubah teks genre menjadi huruf kapital semua, misal `"action"` → `"ACTION"`. Memberikan kesan label/chip yang lebih tegas.

---

```kotlin
Glide.with(root.context)
    .load(film.gambarSampul.ifEmpty { film.gambarPoster })
```
`ifEmpty { }` adalah fungsi Kotlin yang mengembalikan nilai alternatif jika string kosong. Banner mengutamakan `gambarSampul` (gambar landscape) karena lebih cocok untuk banner lebar. Jika tidak ada, fallback ke `gambarPoster`.
