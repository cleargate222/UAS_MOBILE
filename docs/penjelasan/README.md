# Dokumentasi Kode CineApp

Penjelasan lengkap setiap file source code aplikasi CineApp untuk pemula Kotlin.

---

## Struktur Folder

```
docs/penjelasan/
├── model/
│   └── Film.md                    ← Blueprint data film
├── network/
│   └── ApiService.md              ← Komunikasi dengan server API
├── controller/
│   └── FilmController.md          ← Perantara data dan tampilan
├── util/
│   ├── FlexibleIntSerializer.md   ← Konversi angka dari API
│   ├── FlexibleLongSerializer.md  ← Konversi angka besar dari API
│   ├── HistoryManager.md          ← Simpan riwayat film ditonton
│   └── ThemeManager.md            ← Kelola dark/light mode
└── view/
    ├── MainActivity.md            ← Pintu masuk aplikasi
    ├── DetailActivity.md          ← Halaman detail film
    ├── AddEditActivity.md         ← Form tambah/edit film
    ├── FilmAdapter.md             ← Adapter grid kartu film
    ├── BannerAdapter.md           ← Adapter slide carousel
    ├── BannerHeaderAdapter.md     ← Header carousel + auto-scroll
    └── fragment/
        ├── HomeFragment.md        ← Halaman utama
        ├── SearchFragment.md      ← Halaman pencarian
        └── HistoryFragment.md     ← Halaman riwayat
```

---

## Alur Kerja Aplikasi

```
MainActivity
    ├── HomeFragment
    │     ├── BannerHeaderAdapter → BannerAdapter → item_banner.xml
    │     └── FilmAdapter → item_film.xml
    │           ↓ klik
    │         DetailActivity → HistoryManager (simpan riwayat)
    │               ↓ klik edit
    │             AddEditActivity → FilmController → ApiService
    │
    ├── SearchFragment
    │     └── FilmAdapter (filter lokal dari allFilms)
    │
    └── HistoryFragment
          └── FilmAdapter (data dari HistoryManager/SharedPreferences)
```

---

## Lapisan Arsitektur (MVC)

| Lapisan | File | Tanggung Jawab |
|---------|------|----------------|
| **Model** | `Film.kt` | Struktur data |
| **Network** | `ApiService.kt` | Komunikasi API |
| **Controller** | `FilmController.kt` | Logika bisnis, jembatan data-tampilan |
| **View** | Activity, Fragment, Adapter | Tampilan dan interaksi pengguna |
| **Util** | Serializer, HistoryManager, ThemeManager | Fungsi pendukung |
