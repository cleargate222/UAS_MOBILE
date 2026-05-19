# HomeFragment.kt

## Letak File
`app/src/main/java/com/filmapp/view/fragment/HomeFragment.kt`

## Tujuan
Halaman utama aplikasi. Menampilkan **carousel banner** di bagian atas dan **grid semua film** di bawahnya. Juga menyediakan tombol tambah film (FAB) dan toggle tema.

---

## Hubungan dengan File Lain
- **FilmController.kt** → dipanggil untuk mengambil dan menghapus data film
- **BannerHeaderAdapter.kt** → adapter untuk carousel banner
- **FilmAdapter.kt** → adapter untuk grid film
- **DetailActivity.kt** → dibuka saat kartu film diklik
- **AddEditActivity.kt** → dibuka saat FAB (+) diklik
- **ThemeManager.kt** → dipanggil saat tombol tema diklik
- **fragment_home.xml** → layout halaman ini

---

## Siklus Hidup Fragment (Lifecycle)

```
onCreateView → onViewCreated → onResume → onPause → onDestroyView
```

| Fungsi | Kapan dipanggil | Yang dilakukan |
|--------|----------------|----------------|
| `onCreateView` | Fragment pertama dibuat | Inflate layout, kembalikan View |
| `onViewCreated` | Setelah View siap | Setup adapter, tombol, load data |
| `onResume` | Fragment aktif/kembali ke foreground | Reload data, mulai auto-scroll |
| `onPause` | Fragment tidak aktif | Hentikan auto-scroll |
| `onDestroyView` | Fragment dihancurkan | Bersihkan binding |

---

## Penjelasan Kode

```kotlin
private var _binding: FragmentHomeBinding? = null
private val binding get() = _binding!!
```
Pola standar untuk View Binding di Fragment. `_binding` bisa null (saat Fragment tidak punya View). `binding` adalah properti yang selalu non-null — tapi hanya boleh diakses antara `onCreateView` dan `onDestroyView`. `get()` berarti ini adalah computed property yang dievaluasi setiap kali diakses.

---

```kotlin
val concatAdapter = ConcatAdapter(bannerAdapter, filmAdapter)
binding.rvFilms.layoutManager = GridLayoutManager(requireContext(), 2).apply {
    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (position == 0 && bannerAdapter.itemCount > 0) 2 else 1
        }
    }
}
```
`ConcatAdapter` menggabungkan dua adapter. `GridLayoutManager(context, 2)` membuat grid 2 kolom. `SpanSizeLookup` mengatur berapa kolom yang dipakai setiap item:
- Posisi 0 (banner header) → span 2 = full width
- Posisi lainnya (film) → span 1 = setengah lebar

`.apply { }` adalah fungsi ekstensi Kotlin yang menjalankan blok kode pada objek dan mengembalikan objek itu sendiri.

---

```kotlin
lifecycleScope.launch {
    controller.getAllFilms().fold(
        onSuccess = { films -> ... },
        onFailure = { ... }
    )
}
```
`lifecycleScope.launch` menjalankan coroutine yang terikat dengan lifecycle Fragment — otomatis dibatalkan saat Fragment dihancurkan, mencegah memory leak. `launch` memulai coroutine secara asinkron.

---

```kotlin
override fun onDestroyView() {
    super.onDestroyView()
    bannerAdapter.stopAutoScroll()
    _binding = null
}
```
`_binding = null` wajib dilakukan di Fragment untuk mencegah **memory leak** — Fragment bisa hidup lebih lama dari View-nya, dan jika binding tidak di-null-kan, View lama tidak bisa di-garbage collect.
