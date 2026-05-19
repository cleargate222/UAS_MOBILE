# MainActivity.kt

## Letak File
`app/src/main/java/com/filmapp/view/MainActivity.kt`

## Tujuan
**Pintu masuk utama aplikasi**. Activity ini yang pertama kali dibuka saat pengguna menjalankan aplikasi. Tugasnya menampilkan bottom navigation bar dan mengganti-ganti Fragment sesuai tab yang dipilih.

---

## Hubungan dengan File Lain
- **ThemeManager.kt** → dipanggil di awal untuk menerapkan tema tersimpan
- **HomeFragment, SearchFragment, HistoryFragment** → ditampilkan di dalam container sesuai tab yang aktif
- **activity_main.xml** → layout yang digunakan (berisi `fragmentContainer` dan `bottomNav`)

---

## Penjelasan Kode

```kotlin
class MainActivity : AppCompatActivity()
```
`AppCompatActivity` adalah kelas dasar Android untuk Activity yang mendukung fitur-fitur modern (toolbar, tema, dll). `MainActivity` mewarisi semua kemampuannya.

---

```kotlin
private lateinit var binding: ActivityMainBinding
```
`lateinit` berarti variabel ini akan diisi nanti (tidak saat deklarasi). `ActivityMainBinding` adalah kelas yang otomatis dibuat dari file `activity_main.xml` — memungkinkan akses ke semua view tanpa `findViewById`.

---

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ThemeManager.init(this)
    super.onCreate(savedInstanceState)
    ...
}
```
`onCreate` adalah fungsi yang dipanggil Android saat Activity pertama dibuat. `ThemeManager.init(this)` dipanggil **sebelum** `super.onCreate()` agar tema diterapkan sebelum layout dirender. `override` berarti kita menimpa fungsi dari kelas induk.

---

```kotlin
binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
```
`inflate` mengubah file XML layout menjadi objek View yang bisa dimanipulasi. `setContentView` memasang layout tersebut sebagai tampilan Activity.

---

```kotlin
if (savedInstanceState == null) {
    loadFragment(HomeFragment())
}
```
`savedInstanceState` berisi data yang disimpan saat Activity diputar layar atau dikembalikan dari background. Jika `null`, berarti Activity baru pertama dibuka — maka tampilkan `HomeFragment` sebagai default.

---

```kotlin
binding.bottomNav.setOnItemSelectedListener { item ->
    when (item.itemId) {
        R.id.nav_home    -> { loadFragment(HomeFragment()); true }
        R.id.nav_search  -> { loadFragment(SearchFragment()); true }
        R.id.nav_history -> { loadFragment(HistoryFragment()); true }
        else -> false
    }
}
```
Listener yang dipanggil setiap kali pengguna mengetuk tab di bottom navigation. `item.itemId` adalah ID dari menu item yang diklik (didefinisikan di `bottom_nav_menu.xml`). Mengembalikan `true` berarti item berhasil dipilih.

---

```kotlin
private fun loadFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, fragment)
        .commit()
}
```
Mengganti Fragment yang sedang tampil:
- `supportFragmentManager` → pengelola Fragment di Activity
- `beginTransaction()` → mulai transaksi perubahan Fragment
- `.replace(...)` → ganti isi container dengan Fragment baru
- `.commit()` → terapkan perubahan
