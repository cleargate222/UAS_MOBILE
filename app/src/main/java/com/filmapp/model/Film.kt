package com.filmapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

@Parcelize
@Serializable
data class Film(
    @SerialName("id") val id: String = "",
    @SerialName("judul") val judul: String = "",
    @SerialName("ringkasan") val ringkasan: String = "",
    @SerialName("gambar_poster") val gambarPoster: String = "",
    @SerialName("gambar_sampul") val gambarSampul: String = "",
    @Serializable(with = SafeLongSerializer::class)
    @SerialName("tanggal_rilis") val tanggalRilis: Long = 0L,
    @Serializable(with = SafeIntSerializer::class)
    @SerialName("skor_rating") val skorRating: Int = 0,
    @SerialName("kategori") val kategori: String = "",
    @SerialName("url_trailer") val urlTrailer: String = ""
) : Parcelable {
    // Helper untuk tampilan
    val tahunRilis: String get() {
        if (tanggalRilis == 0L) return "-"
        // Handle case where it might be a year already (e.g. 2014) vs timestamp
        if (tanggalRilis > 3000) {
            val date = java.util.Date(tanggalRilis * 1000)
            return java.text.SimpleDateFormat("yyyy", java.util.Locale.getDefault()).format(date)
        }
        return tanggalRilis.toString()
    }
    val ratingDisplay: String get() = "⭐ ${skorRating}/100"
}

object SafeLongSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SafeLong", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Long) = encoder.encodeLong(value)
    override fun deserialize(decoder: Decoder): Long {
        val input = decoder as? JsonDecoder ?: return decoder.decodeLong()
        val element = input.decodeJsonElement()
        return if (element is JsonPrimitive) {
            // Try as long, then try as string, then try to parse ISO date if possible
            element.longOrNull ?: element.content.toLongOrNull() ?: tryParseIsoDate(element.content) ?: 0L
        } else 0L
    }

    private fun tryParseIsoDate(content: String): Long? {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            sdf.parse(content)?.time?.div(1000)
        } catch (e: Exception) {
            null
        }
    }
}

object SafeIntSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SafeInt", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: Int) = encoder.encodeInt(value)
    override fun deserialize(decoder: Decoder): Int {
        val input = decoder as? JsonDecoder ?: return decoder.decodeInt()
        val element = input.decodeJsonElement()
        return if (element is JsonPrimitive) {
            element.intOrNull ?: element.content.toIntOrNull() ?: 0
        } else 0
    }
}
