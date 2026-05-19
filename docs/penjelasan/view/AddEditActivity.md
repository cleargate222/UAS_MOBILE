# AddEditActivity.kt

## Letak File
`app/src/main/java/com/filmapp/view/AddEditActivity.kt`

## Tujuan
Halaman **formulir** untuk menambah film baru atau mengedit film yang sudah ada. Satu halaman digunakan untuk dua fungsi — dibedakan berdasarkan ada tidaknya data film yang dikirim.

---

## Hubungan dengan File Lain
- **FilmController.kt** → dipanggil untuk `createFilm` atau `updateFilm`
- **Film.kt** → data yang diisi form diubah menjadi objek `Film`
- **HomeFragment.kt** → membuka halaman ini saat FAB (+) ditekan
- **DetailActivity.kt** → membuka halaman ini saat tombol "Edit Film" ditekan
- **activity_add_edit.xml** → layout formulir

---

## Penjelasan Kode

```kotlin
@Suppress("DEPRECATION")
existingFilm = intent.getParcelableExtra("film")
```
Mengambil data film dari Intent. Jika `existingFilm` tidak null, berarti mode **edit**. Jika null, berarti mode **tambah baru**. `@Suppress("DEPRECATION")` menekan peringatan karena `getParcelableExtra` versi lama digunakan untuk kompatibilitas.

---

```kotlin
supportActionBar?.title = if (existingFilm != null) "Edit Film" else "Tambah Film"
```
Mengubah judul toolbar secara dinamis. `?` setelah `supportActionBar` adalah safe call — hanya dieksekusi jika tidak null. `if-else` di Kotlin bisa digunakan sebagai ekspresi (mengembalikan nilai).

---

```kotlin
existingFilm?.let { film ->
    binding.etTitle.setText(film.judul)
    ...
}
```
`?.let { }` hanya dieksekusi jika `existingFilm` tidak null. Di dalam blok, `film` adalah alias untuk `existingFilm`. Ini cara idiomatis Kotlin untuk menghindari pengecekan null berulang.

---

```kotlin
val yearInput = binding.etYear.text.toString().trim().toIntOrNull() ?: 2026
val calendar = java.util.Calendar.getInstance()
calendar.set(java.util.Calendar.YEAR, yearInput)
...
val releaseTimestamp = calendar.timeInMillis / 1000
```
Mengubah input tahun (misal `2023`) menjadi Unix timestamp:
1. Parse teks input ke Int, default `2026` jika kosong
2. Buat objek Calendar dan set tahunnya
3. Ambil waktu dalam milidetik, bagi 1000 untuk mendapat detik (format Unix timestamp)

---

```kotlin
val result = if (existingFilm != null) {
    controller.updateFilm(existingFilm!!.id, film.copy(id = existingFilm!!.id))
} else {
    controller.createFilm(film.copy(id = ""))
}
```
Memilih operasi berdasarkan mode:
- Edit → `updateFilm` dengan ID film yang sudah ada
- Tambah → `createFilm` dengan ID kosong (server yang menentukan ID baru)
- `film.copy(id = ...)` → membuat salinan objek `Film` dengan field `id` yang diubah, field lain tetap sama
- `!!` adalah **non-null assertion** — memaksa Kotlin memperlakukan nilai sebagai non-null (berisiko crash jika ternyata null, tapi di sini aman karena sudah dicek)

---

```kotlin
result.fold(
    onSuccess = { finish() },
    onFailure = { ... }
)
```
`finish()` menutup Activity dan kembali ke halaman sebelumnya setelah berhasil menyimpan.
