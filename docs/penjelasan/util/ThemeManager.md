# ThemeManager.kt

## Letak File
`app/src/main/java/com/filmapp/util/ThemeManager.kt`

## Tujuan
Mengelola **mode tampilan gelap (dark mode) dan terang (light mode)**. Menyimpan preferensi pengguna agar tema yang dipilih tetap diingat saat aplikasi dibuka kembali.

---

## Hubungan dengan File Lain
- **MainActivity.kt** → memanggil `ThemeManager.init()` saat aplikasi pertama dibuka
- **HomeFragment.kt** → memanggil `ThemeManager.toggle()` saat tombol tema ditekan

---

## Penjelasan Kode

```kotlin
object ThemeManager
```
Singleton — satu instance untuk seluruh aplikasi.

---

```kotlin
fun isDarkMode(context: Context): Boolean {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_DARK, true)
}
```
Membaca preferensi tema dari SharedPreferences. Nilai default `true` berarti **dark mode aktif secara default** saat pertama kali install.

---

```kotlin
fun toggle(context: Context) {
    val isDark = isDarkMode(context)
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(KEY_DARK, !isDark).apply()
    applyTheme(!isDark)
}
```
Membalik tema saat ini:
- Baca status tema sekarang
- Simpan kebalikannya (`!isDark` → jika sekarang dark, simpan `false`)
- Terapkan tema baru

`!` adalah operator **NOT** — membalik nilai boolean.

---

```kotlin
fun applyTheme(isDark: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (isDark) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
    )
}
```
Menerapkan tema ke seluruh aplikasi menggunakan `AppCompatDelegate` dari library AndroidX. `MODE_NIGHT_YES` = dark mode, `MODE_NIGHT_NO` = light mode. Perubahan ini langsung terlihat tanpa perlu restart aplikasi.

---

```kotlin
fun init(context: Context) = applyTheme(isDarkMode(context))
```
Dipanggil saat aplikasi pertama dibuka (di `MainActivity.onCreate`). Membaca preferensi yang tersimpan dan menerapkan tema yang sesuai. Harus dipanggil **sebelum** `super.onCreate()` agar tema diterapkan sebelum layout dibuat.
