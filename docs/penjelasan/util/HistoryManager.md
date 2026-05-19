# HistoryManager.kt

## Letak File
`app/src/main/java/com/filmapp/util/HistoryManager.kt`

## Tujuan
Menyimpan dan mengelola **riwayat film yang pernah dibuka** oleh pengguna. Data disimpan secara lokal di perangkat menggunakan `SharedPreferences`, sehingga tetap ada meski aplikasi ditutup.

---

## Hubungan dengan File Lain
- **DetailActivity.kt** → memanggil `HistoryManager.add()` setiap kali pengguna membuka detail film
- **HistoryFragment.kt** → memanggil `HistoryManager.getAll()` untuk menampilkan daftar riwayat, dan `HistoryManager.clear()` untuk menghapus semua

---

## Penjelasan Kode

```kotlin
object HistoryManager
```
`object` = Singleton. Hanya ada satu HistoryManager di seluruh aplikasi, tidak perlu membuat instance baru.

---

```kotlin
private const val PREF_NAME = "history_pref"
private const val KEY_HISTORY = "history"
private const val MAX_HISTORY = 20
```
Konstanta konfigurasi:
- `PREF_NAME` → nama file SharedPreferences yang digunakan
- `KEY_HISTORY` → key untuk menyimpan data di dalam file tersebut
- `MAX_HISTORY` → maksimal 20 film tersimpan di riwayat

---

```kotlin
fun add(context: Context, film: Film) {
    val list = getAll(context).toMutableList()
    list.removeAll { it.id == film.id }
    list.add(0, film)
    if (list.size > MAX_HISTORY) list.removeAt(list.lastIndex)
    save(context, list)
}
```
Logika penambahan riwayat:
1. Ambil daftar riwayat yang sudah ada
2. Hapus film yang sama jika sudah ada (agar tidak duplikat)
3. Tambahkan film baru di posisi paling atas (`index 0`)
4. Jika sudah lebih dari 20, hapus yang paling lama (index terakhir)
5. Simpan kembali

`{ it.id == film.id }` adalah **lambda** — fungsi singkat tanpa nama. `it` merujuk ke elemen saat ini dalam iterasi.

---

```kotlin
fun getAll(context: Context): List<Film> {
    val json = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .getString(KEY_HISTORY, null) ?: return emptyList()
    return runCatching { Json.decodeFromString<List<Film>>(json) }.getOrDefault(emptyList())
}
```
Membaca riwayat dari SharedPreferences:
- `getString(KEY_HISTORY, null)` → ambil teks JSON, jika tidak ada kembalikan `null`
- `?: return emptyList()` → jika null, langsung kembalikan list kosong
- `Json.decodeFromString<List<Film>>(json)` → ubah teks JSON menjadi list objek `Film`
- `runCatching { }.getOrDefault(emptyList())` → jika parsing gagal, kembalikan list kosong (tidak crash)

---

```kotlin
private fun save(context: Context, list: List<Film>) {
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit().putString(KEY_HISTORY, Json.encodeToString(list)).apply()
}
```
Menyimpan list film ke SharedPreferences sebagai teks JSON:
- `Json.encodeToString(list)` → ubah list `Film` menjadi teks JSON
- `.edit()` → buka mode edit SharedPreferences
- `.putString(...)` → simpan teks
- `.apply()` → terapkan perubahan secara asinkron (tidak memblokir UI)
