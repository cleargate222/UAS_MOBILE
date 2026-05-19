# FlexibleLongSerializer.kt

## Letak File
`app/src/main/java/com/filmapp/util/FlexibleLongSerializer.kt`

## Tujuan
Sama seperti `FlexibleIntSerializer`, tapi untuk tipe data `Long` (angka sangat besar). Digunakan khusus untuk field `tanggalRilis` yang berupa Unix timestamp — angka yang terlalu besar untuk `Int`.

---

## Hubungan dengan File Lain
- **Film.kt** → digunakan untuk field `tanggalRilis` via `@Serializable(with = FlexibleLongSerializer::class)`

---

## Mengapa Long, Bukan Int?
Unix timestamp adalah jumlah detik sejak 1 Januari 1970. Nilainya saat ini sekitar `1.7 miliar` — terlalu besar untuk `Int` (maksimum ~2.1 miliar, tapi bisa overflow). `Long` bisa menampung hingga ~9.2 kuadriliun.

---

## Penjelasan Kode

```kotlin
object FlexibleLongSerializer : KSerializer<Long>
```
Sama dengan `FlexibleIntSerializer` tapi untuk tipe `Long`.

---

```kotlin
element.isString ->
    element.content.toLongOrNull()
        ?: element.content.toDoubleOrNull()?.toLong()
        ?: 0L
```
Mencoba konversi teks ke Long. Jika gagal, coba ke Double dulu lalu ke Long. Jika tetap gagal (misal API mengirim `"2026-05-12"` yang tidak bisa jadi angka), kembalikan `0L`. Huruf `L` di akhir angka menandakan tipe `Long` di Kotlin.

---

## Perbedaan dengan FlexibleIntSerializer

| | FlexibleIntSerializer | FlexibleLongSerializer |
|--|--|--|
| Tipe target | `Int` | `Long` |
| Digunakan untuk | `skorRating` | `tanggalRilis` |
| Nilai default | `0` | `0L` |
| Fungsi konversi | `toIntOrNull()` | `toLongOrNull()` |
