# DetailActivity.kt

## Letak File
`app/src/main/java/com/filmapp/view/DetailActivity.kt`

## Tujuan
Menampilkan **halaman detail lengkap** sebuah film — poster, judul, genre, rating, sinopsis, dan pemutar video trailer. Juga mencatat film yang dibuka ke riwayat.

---

## Hubungan dengan File Lain
- **Film.kt** → menerima objek `Film` dari Intent untuk ditampilkan
- **HistoryManager.kt** → dipanggil untuk mencatat film ke riwayat
- **AddEditActivity.kt** → dibuka saat tombol "Edit Film" ditekan
- **activity_detail.xml** → layout halaman ini

---

## Penjelasan Kode

```kotlin
val film = intent.getParcelableExtra<Film>("film") ?: return
```
Mengambil objek `Film` yang dikirim dari halaman sebelumnya via `Intent`. `?: return` berarti jika film `null` (tidak ada data), langsung keluar dari fungsi — mencegah crash.

---

```kotlin
HistoryManager.add(this, film)
```
Setiap kali halaman detail dibuka, film otomatis dicatat ke riwayat. `this` merujuk ke Activity saat ini (dibutuhkan untuk akses SharedPreferences).

---

```kotlin
Glide.with(this)
    .load(film.gambarSampul.ifEmpty { film.gambarPoster })
    .placeholder(R.drawable.ic_movie_placeholder)
    .error(R.drawable.ic_movie_placeholder)
    .centerCrop()
    .into(binding.ivPoster)
```
Memuat gambar dari URL menggunakan library **Glide**:
- `.load(...)` → URL gambar. `ifEmpty { }` berarti jika `gambarSampul` kosong, gunakan `gambarPoster`
- `.placeholder(...)` → gambar sementara saat loading
- `.error(...)` → gambar yang ditampilkan jika URL gagal dimuat
- `.centerCrop()` → potong gambar agar mengisi area tanpa distorsi
- `.into(...)` → target ImageView

---

```kotlin
val mediaController = MediaController(this)
mediaController.setAnchorView(binding.videoView)
binding.videoView.setMediaController(mediaController)
binding.videoView.setVideoPath(film.urlTrailer)
binding.videoView.setOnPreparedListener { mp ->
    binding.pbVideoLoading.visibility = View.GONE
    binding.videoView.start()
}
```
Setup pemutar video:
- `MediaController` → kontrol play/pause/seek yang muncul saat video disentuh
- `setAnchorView` → posisi kontrol mengikuti VideoView
- `setVideoPath` → URL video yang akan diputar
- `setOnPreparedListener` → dipanggil saat video siap diputar; sembunyikan loading dan mulai putar otomatis

---

```kotlin
override fun onPause() {
    super.onPause()
    if (binding.videoView.isPlaying) binding.videoView.pause()
}

override fun onDestroy() {
    super.onDestroy()
    binding.videoView.stopPlayback()
}
```
Manajemen lifecycle video:
- `onPause` → jeda video saat pengguna pindah aplikasi
- `onDestroy` → hentikan dan bebaskan resource video saat halaman ditutup
