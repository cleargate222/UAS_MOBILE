# FilmApp 🎬

Aplikasi Android untuk mengelola dan menjelajahi koleksi film. Dibangun dengan Kotlin menggunakan arsitektur MVC, Ktor sebagai HTTP client, dan MockAPI sebagai backend.

---

## Fitur Utama

- **Daftar Film** — Menampilkan semua film dari API dalam tampilan grid 2 kolom
- **Detail Film** — Melihat informasi lengkap film: poster, genre, tahun rilis, rating, dan ringkasan
- **Tambah Film** — Menambahkan data film baru ke server
- **Edit Film** — Memperbarui data film yang sudah ada
- **Hapus Film** — Menghapus film dengan konfirmasi dialog
- **Pencarian** — Filter film secara real-time berdasarkan judul, genre, atau ringkasan
- **Riwayat Tontonan** — Menyimpan film yang pernah dibuka (maks. 20 film), tersimpan lokal
- **Toggle Tema** — Beralih antara mode gelap dan terang, preferensi disimpan otomatis

---

## Teknologi yang Digunakan

| Komponen | Library / Tool |
|---|---|
| Bahasa | Kotlin |
| UI | ViewBinding, Material Components, ConstraintLayout |
| HTTP Client | Ktor 2.3.7 (Android engine) |
| Serialisasi | Kotlinx Serialization |
| Async | Kotlin Coroutines |
| Image Loading | Glide 4.16.0 |
| Lifecycle | AndroidX Lifecycle KTX |
| Backend | MockAPI (REST) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

---

## Arsitektur

Aplikasi menggunakan pola **MVC (Model-View-Controller)**:

```
com.filmapp
├── model/
│   └── Film.kt              # Data class film (Parcelable + Serializable)
├── network/
│   └── ApiService.kt        # Ktor HTTP client, semua request ke API
├── controller/
│   └── FilmController.kt    # Logika bisnis, jembatan antara View dan ApiService
├── view/
│   ├── MainActivity.kt      # Activity utama dengan Bottom Navigation
│   ├── DetailActivity.kt    # Halaman detail film
│   ├── AddEditActivity.kt   # Form tambah / edit film
│   ├── FilmAdapter.kt       # RecyclerView adapter untuk daftar film
│   └── fragment/
│       ├── HomeFragment.kt     # Tab beranda — daftar semua film
│       ├── SearchFragment.kt   # Tab pencarian — filter real-time
│       └── HistoryFragment.kt  # Tab riwayat — film yang pernah dibuka
└── util/
    ├── HistoryManager.kt    # Manajemen riwayat via SharedPreferences
    └── ThemeManager.kt      # Manajemen tema gelap/terang
```

---

## Alur Navigasi

```
MainActivity (Bottom Navigation)
├── HomeFragment     → DetailActivity → AddEditActivity (edit)
│                    → AddEditActivity (tambah baru via FAB)
├── SearchFragment   → DetailActivity
└── HistoryFragment  → DetailActivity
```

---

## Model Data Film

| Field | Tipe | Keterangan |
|---|---|---|
| `id` | String | ID unik dari API |
| `judul` | String | Judul film |
| `ringkasan` | String | Sinopsis / deskripsi |
| `gambar_poster` | String | URL gambar poster |
| `gambar_sampul` | String | URL gambar sampul (banner) |
| `tanggal_rilis` | Long | Unix timestamp tanggal rilis |
| `skor_rating` | Int | Skor rating (0–100) |
| `kategori` | String | Genre film |
| `url_trailer` | String | URL trailer film |

---

## API Endpoint

Base URL: `https://68ff8dfbe02b16d1753e765d.mockapi.io/film`

| Method | Endpoint | Fungsi |
|---|---|---|
| GET | `/film` | Ambil semua film |
| GET | `/film/{id}` | Ambil film berdasarkan ID |
| POST | `/film` | Tambah film baru |
| PUT | `/film/{id}` | Perbarui data film |
| DELETE | `/film/{id}` | Hapus film |

---

## Cara Menjalankan

### Prasyarat
- Android Studio Hedgehog atau lebih baru
- JDK 11
- Koneksi internet (untuk mengakses MockAPI)

### Langkah

1. Clone repositori ini:
   ```bash
   git clone <url-repositori>
   ```

2. Buka project di Android Studio

3. Tunggu Gradle sync selesai

4. Jalankan di emulator atau perangkat fisik (min. Android 7.0):
   ```
   Run > Run 'app'
   ```

---

## Screenshot Fitur

| Beranda | Pencarian | Riwayat | Detail |
|---|---|---|---|
| Grid daftar film | Filter real-time | Film yang pernah dibuka | Info lengkap film |

---

## Catatan

- Data film disimpan di MockAPI (cloud), bukan database lokal
- Riwayat tontonan disimpan di SharedPreferences perangkat (maks. 20 entri)
- Preferensi tema (gelap/terang) juga disimpan lokal di SharedPreferences
- Default tema saat pertama install adalah **mode gelap**
