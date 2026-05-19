# Film.kt

## Letak File
`app/src/main/java/com/filmapp/model/Film.kt`

## Tujuan
File ini adalah **blueprint** atau cetakan data film. Semua bagian aplikasi yang butuh data film (judul, poster, rating, dll) menggunakan struktur yang didefinisikan di sini.

---

## Hubungan dengan File Lain
- **ApiService.kt** → mengubah JSON dari internet menjadi objek `Film`
- **FilmController.kt** → meneruskan objek `Film` ke tampilan
- **FilmAdapter, BannerAdapter** → membaca properti `Film` untuk ditampilkan di layar
- **HistoryManager.kt** → menyimpan dan membaca daftar `Film` ke memori lokal
- **AddEditActivity, DetailActivity** → menerima dan mengirim objek `Film` antar halaman

---

## Penjelasan Kode

```kotlin
package com.filmapp.model
```
Mendeklarasikan bahwa file ini berada di dalam kelompok (package) `model`. Package adalah cara mengelompokkan file yang punya fungsi serupa.

---

```kotlin
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
```
`Parcelable` adalah kemampuan Android untuk **mengirim objek antar halaman** (Activity/Fragment). Tanpa ini, kita tidak bisa mengirim data film dari halaman daftar ke halaman detail.

---

```kotlin
@InternalSerializationApi
@Parcelize
@Serializable
data class Film(...) : Parcelable
```

| Bagian | Penjelasan |
|--------|-----------|
| `@InternalSerializationApi` | Tanda bahwa kelas ini menggunakan fitur serialisasi internal. Wajib ada karena library serialisasi memintanya. |
| `@Parcelize` | Otomatis membuat kode untuk mengirim objek `Film` antar halaman. |
| `@Serializable` | Menandai bahwa objek ini bisa diubah ke/dari format JSON. |
| `data class` | Tipe kelas khusus di Kotlin untuk menyimpan data. Otomatis punya fungsi `equals`, `copy`, `toString`. |
| `: Parcelable` | Menyatakan bahwa `Film` mengikuti aturan `Parcelable`. |

---

```kotlin
@SerialName("judul")
val judul: String = ""
```
`@SerialName("judul")` memberitahu library bahwa field `judul` di Kotlin sesuai dengan key `"judul"` di JSON dari API. Nilai default `""` berarti jika API tidak mengirim field ini, nilainya kosong.

---

```kotlin
@Serializable(with = FlexibleLongSerializer::class)
@SerialName("tanggal_rilis")
val tanggalRilis: Long = 0L
```
API kadang mengirim tanggal sebagai angka, kadang sebagai teks. `FlexibleLongSerializer` menangani kedua kemungkinan itu. `Long` adalah tipe angka besar (cocok untuk timestamp Unix).

---

```kotlin
val tahunRilis: String get() {
    if (tanggalRilis == 0L) return "-"
    val date = java.util.Date(tanggalRilis * 1000)
    return java.text.SimpleDateFormat("yyyy", ...).format(date)
}
```
Ini adalah **computed property** — nilainya dihitung setiap kali dipanggil, bukan disimpan. `tanggalRilis` adalah Unix timestamp (detik sejak 1970), dikali 1000 karena `Date` butuh milidetik. Hasilnya diformat menjadi tahun saja, misal `"2023"`.

---

```kotlin
val ratingDisplay: String get() = "⭐ ${skorRating}/100"
```
Mengubah angka rating (misal `85`) menjadi teks siap tampil `"⭐ 85/100"`. `${}` adalah **string template** Kotlin untuk menyisipkan nilai variabel ke dalam teks.
