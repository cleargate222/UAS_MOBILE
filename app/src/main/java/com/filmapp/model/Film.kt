package com.filmapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Film(
    @SerialName("id") val id: String = "",
    @SerialName("judul") val judul: String = "",
    @SerialName("ringkasan") val ringkasan: String = "",
    @SerialName("gambar_poster") val gambarPoster: String = "",
    @SerialName("gambar_sampul") val gambarSampul: String = "",
    @SerialName("tanggal_rilis") val tanggalRilis: Long = 0L,
    @SerialName("skor_rating") val skorRating: Int = 0,
    @SerialName("kategori") val kategori: String = "",
    @SerialName("url_trailer") val urlTrailer: String = ""
) : Parcelable {
    // Helper untuk tampilan
    val tahunRilis: String get() {
        if (tanggalRilis == 0L) return "-"
        val date = java.util.Date(tanggalRilis * 1000)
        return java.text.SimpleDateFormat("yyyy", java.util.Locale.getDefault()).format(date)
    }
    val ratingDisplay: String get() = "⭐ ${skorRating}/100"
}
