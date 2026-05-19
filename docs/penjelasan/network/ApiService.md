# ApiService.kt

## Letak File
`app/src/main/java/com/filmapp/network/ApiService.kt`

## Tujuan
File ini adalah **jembatan antara aplikasi dan internet**. Semua komunikasi dengan server (ambil data, simpan, edit, hapus film) dilakukan di sini menggunakan library **Ktor**.

---

## Hubungan dengan File Lain
- **FilmController.kt** → memanggil fungsi-fungsi di `ApiService` untuk mendapatkan data
- **Film.kt** → hasil dari API diubah menjadi objek `Film`

---

## Penjelasan Kode

```kotlin
object ApiService
```
`object` di Kotlin adalah **Singleton** — hanya ada satu instance di seluruh aplikasi. Cocok untuk service yang tidak perlu dibuat berkali-kali.

---

```kotlin
private const val BASE_URL = "https://68ff8dfbe02b16d1753e765d.mockapi.io/film"
```
Alamat server API. `const val` berarti nilai ini tetap (tidak bisa diubah) dan diketahui saat kompilasi. `private` berarti hanya bisa diakses dari dalam file ini.

---

```kotlin
private val client = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        })
    }
    install(Logging) { level = LogLevel.BODY }
}
```
Membuat klien HTTP (seperti browser mini). Konfigurasinya:

| Opsi | Penjelasan |
|------|-----------|
| `ignoreUnknownKeys = true` | Jika API mengirim field yang tidak ada di `Film.kt`, abaikan saja (tidak error) |
| `isLenient = true` | Lebih toleran terhadap format JSON yang tidak sempurna |
| `coerceInputValues = true` | Jika tipe data tidak cocok, coba konversi otomatis |
| `Logging` | Mencatat semua request/response di logcat untuk debugging |

---

```kotlin
suspend fun getAllFilms(): List<Film> = client.get(BASE_URL).body()
```
`suspend` berarti fungsi ini berjalan secara **asinkron** — tidak memblokir aplikasi saat menunggu respons internet. `client.get(BASE_URL)` mengirim HTTP GET request. `.body()` mengubah respons JSON menjadi `List<Film>`.

---

```kotlin
suspend fun createFilm(film: Film): Film = client.post(BASE_URL) {
    contentType(ContentType.Application.Json)
    setBody(film)
}.body()
```
Mengirim HTTP POST untuk membuat film baru. `setBody(film)` mengubah objek `Film` menjadi JSON dan mengirimnya ke server. `contentType` memberitahu server bahwa kita mengirim JSON.

---

```kotlin
suspend fun updateFilm(id: String, film: Film): Film = client.put("$BASE_URL/$id") { ... }
suspend fun deleteFilm(id: String): Film = client.delete("$BASE_URL/$id").body()
```
- `put` → HTTP PUT untuk mengupdate data yang sudah ada (menggunakan ID spesifik)
- `delete` → HTTP DELETE untuk menghapus film berdasarkan ID
- `"$BASE_URL/$id"` → string template, hasilnya misal `".../film/123"`
