package com.filmapp.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object FlexibleLongSerializer : KSerializer<Long> {

    override val descriptor = PrimitiveSerialDescriptor(
        "FlexibleLong", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Long {
        val jsonDecoder = decoder as? JsonDecoder
            ?: return decoder.decodeLong()

        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> when {
                // Nilai null dari API → default 0
                element.content == "null" || element.jsonPrimitive.isString
                        && element.content.isEmpty() -> 0L

                // String number: "1778301247" → 1778301247L
                element.isString ->
                    element.content.toLongOrNull()
                        ?: element.content.toDoubleOrNull()?.toLong()
                        ?: 0L // "2026-05-12" tidak bisa jadi Long → 0

                // Number biasa: 1778301247
                else -> element.longOrNull ?: 0L
            }
            else -> 0L
        }
    }

    override fun serialize(encoder: Encoder, value: Long) =
        encoder.encodeLong(value)
}