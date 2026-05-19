# BannerHeaderAdapter.kt

## Letak File
`app/src/main/java/com/filmapp/view/BannerHeaderAdapter.kt`

## Tujuan
Adapter yang berperan sebagai **header RecyclerView** di halaman Home. Menampilkan satu item berisi ViewPager2 (carousel) beserta dot indikator, dan mengelola auto-scroll otomatis setiap 3 detik.

---

## Hubungan dengan File Lain
- **BannerAdapter.kt** → digunakan di dalam ViewPager2 untuk menampilkan setiap slide
- **HomeFragment.kt** → membuat dan mengontrol adapter ini via `ConcatAdapter`
- **layout_banner_header.xml** → layout yang berisi ViewPager2 dan dot container

---

## Konsep ConcatAdapter
`HomeFragment` menggabungkan dua adapter menjadi satu menggunakan `ConcatAdapter`:
```
ConcatAdapter(
    BannerHeaderAdapter  ← 1 item (header carousel)
    FilmAdapter          ← banyak item (grid film)
)
```
RecyclerView melihatnya sebagai satu daftar, tapi sebenarnya dari dua sumber berbeda.

---

## Penjelasan Kode

```kotlin
private val handler = Handler(Looper.getMainLooper())
private val autoScrollRunnable = object : Runnable {
    override fun run() {
        currentPage = (currentPage + 1) % films.size
        vp.setCurrentItem(currentPage, true)
        handler.postDelayed(this, AUTO_SCROLL_DELAY)
    }
}
```
Mekanisme auto-scroll:
- `Handler` + `Runnable` adalah cara Android menjalankan kode secara berulang dengan jeda waktu
- `(currentPage + 1) % films.size` → modulo memastikan setelah halaman terakhir kembali ke 0
- `handler.postDelayed(this, 3000L)` → jadwalkan `Runnable` ini lagi 3 detik kemudian
- `object : Runnable { }` → membuat implementasi anonymous dari interface `Runnable`

---

```kotlin
override fun getItemCount() = if (films.isEmpty()) 0 else 1
```
Selalu mengembalikan `1` (satu header) jika ada film, atau `0` jika belum ada data. Ini penting agar header tidak muncul saat data masih loading.

---

```kotlin
fun updateFilms(newFilms: List<Film>) {
    films = newFilms.take(5)
    currentPage = 0
    notifyDataSetChanged()
}
```
`.take(5)` mengambil maksimal 5 item pertama dari list. Banner tidak perlu menampilkan semua film, cukup beberapa yang paling relevan.

---

```kotlin
val typedValue = TypedValue()
context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
val colorOnSurface = typedValue.data
```
Cara membaca warna dari tema aktif secara programatik. `resolveAttribute` mencari nilai atribut tema (`colorOnSurface`) dan menyimpannya ke `TypedValue`. Ini digunakan agar warna dot inactive otomatis menyesuaikan light/dark mode.

---

```kotlin
dot.setColorFilter(colorOnSurface)
dot.alpha = 0.3f
```
Menerapkan warna dan transparansi ke dot inactive. `setColorFilter` mengganti warna drawable. `alpha` mengatur transparansi (0.0 = transparan penuh, 1.0 = solid). `f` di akhir angka menandakan tipe `Float`.

---

```kotlin
fun startAutoScroll() {
    handler.removeCallbacks(autoScrollRunnable)
    if (films.size > 1) handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
}

fun stopAutoScroll() {
    handler.removeCallbacks(autoScrollRunnable)
}
```
`removeCallbacks` memastikan tidak ada duplikat runnable yang berjalan. `startAutoScroll` dipanggil di `onResume` Fragment, `stopAutoScroll` di `onPause` — agar auto-scroll berhenti saat aplikasi di-background dan tidak membuang baterai.
