# FilmController.kt

## Letak File
`app/src/main/java/com/filmapp/controller/FilmController.kt`

## Tujuan
File ini adalah **perantara antara tampilan (View) dan data (ApiService)**. Tampilan tidak boleh langsung memanggil API — semua harus lewat Controller. Ini mengikuti pola arsitektur **MVC (Model-View-Controller)**.

---

## Hubungan dengan File Lain
- **ApiService.kt** → dipanggil oleh Controller untuk komunikasi ke server
- **HomeFragment, SearchFragment, AddEditActivity, DetailActivity** → memanggil fungsi Controller untuk mendapatkan atau mengubah data

---

## Alur Data
```
Tampilan (Fragment/Activity)
    ↓ memanggil
FilmController
    ↓ memanggil
ApiService
    ↓ request ke
Server API
```

---

## Penjelasan Kode

```kotlin
class FilmController
```
`class` biasa (bukan `object`) karena setiap Fragment/Activity membuat instance-nya sendiri dengan `val controller = FilmController()`.

---

```kotlin
suspend fun getAllFilms(): Result<List<Film>> = withContext(Dispatchers.IO) {
    runCatching { ApiService.getAllFilms() }
}
```

| Bagian | Penjelasan |
|--------|-----------|
| `suspend` | Fungsi asinkron, harus dipanggil dari dalam coroutine |
| `Result<List<Film>>` | Tipe kembalian yang bisa berisi **sukses** (dengan data) atau **gagal** (dengan error) |
| `withContext(Dispatchers.IO)` | Menjalankan kode di thread khusus untuk operasi I/O (internet, file), bukan di thread UI |
| `runCatching { }` | Menjalankan kode di dalamnya dan menangkap error jika ada, lalu membungkusnya dalam `Result` |

---

```kotlin
result.fold(
    onSuccess = { films -> ... },
    onFailure = { error -> ... }
)
```
Di sisi tampilan, `Result` dibuka dengan `.fold()`. Jika sukses, blok `onSuccess` dijalankan dengan data filmnya. Jika gagal, blok `onFailure` dijalankan dengan pesan errornya.

---

## Mengapa Perlu Controller?
Tanpa Controller, jika kita ingin mengubah cara mengambil data (misal dari API ke database lokal), kita harus mengubah semua Fragment/Activity. Dengan Controller, cukup ubah satu file ini saja.
