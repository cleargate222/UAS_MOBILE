# FlexibleIntSerializer.kt

## Letak File
`app/src/main/java/com/filmapp/util/FlexibleIntSerializer.kt`

## Tujuan
Menangani situasi di mana API mengirim angka integer dalam berbagai format yang tidak konsisten — bisa berupa angka biasa, teks, atau desimal. Tanpa file ini, aplikasi akan crash jika API mengirim `"85"` (teks) padahal kita mengharapkan `85` (angka).

---

## Hubungan dengan File Lain
- **Film.kt** → digunakan untuk field `skorRating` via `@Serializable(with = FlexibleIntSerializer::class)`

---

## Penjelasan Kode

```kotlin
object FlexibleIntSerializer : KSerializer<Int>
```
`KSerializer<Int>` adalah kontrak dari library serialisasi yang harus dipenuhi. Dengan mengimplementasikannya, kita mendefinisikan sendiri cara membaca (`deserialize`) dan menulis (`serialize`) nilai `Int`.

---

```kotlin
override val descriptor = PrimitiveSerialDescriptor("FlexibleInt", PrimitiveKind.STRING)
```
Memberitahu library bahwa serializer ini bekerja dengan tipe primitif berbentuk string. Ini diperlukan agar library tahu cara mendeskripsikan tipe data ini.

---

```kotlin
override fun deserialize(decoder: Decoder): Int {
    val jsonDecoder = decoder as? JsonDecoder
        ?: return decoder.decodeInt()
```
`as?` adalah **safe cast** di Kotlin — mencoba mengubah tipe, tapi jika gagal mengembalikan `null` bukan crash. `?: return decoder.decodeInt()` artinya: jika bukan JsonDecoder, gunakan cara baca biasa.

---

```kotlin
return when (val element = jsonDecoder.decodeJsonElement()) {
    is JsonPrimitive -> when {
        element.content == "null" || element.content.isEmpty() -> 0
        element.isString -> element.content.toIntOrNull()
            ?: element.content.toDoubleOrNull()?.toInt()
            ?: 0
        else -> element.intOrNull ?: 0
    }
    else -> 0
}
```

`when` adalah versi Kotlin dari `switch-case`. Logika pembacaannya:

| Kondisi | Hasil |
|---------|-------|
| Nilai `"null"` atau kosong | Kembalikan `0` |
| Teks angka seperti `"85"` | Konversi ke Int |
| Teks desimal seperti `"6.7"` | Konversi ke Double dulu, lalu bulatkan ke Int (`6`) |
| Angka biasa `85` | Baca langsung |
| Tidak dikenali | Kembalikan `0` |

`?.` adalah **safe call** — hanya memanggil fungsi jika nilai sebelumnya bukan null. `?:` adalah **Elvis operator** — jika kiri null, gunakan nilai kanan.
