package com.filmapp.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object FlexibleIntSerializer : KSerializer<Int> {

    override val descriptor = PrimitiveSerialDescriptor(
        "FlexibleInt", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Int {
        val jsonDecoder = decoder as? JsonDecoder
            ?: return decoder.decodeInt()

        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> when {
                element.content == "null" || element.isString
                        && element.content.isEmpty() -> 0

                element.isString ->
                    element.content.toIntOrNull()
                    // "6.7" → 6 (bulatkan ke bawah)
                        ?: element.content.toDoubleOrNull()?.toInt()
                        ?: 0

                else -> element.intOrNull ?: 0
            }
            else -> 0
        }
    }

    override fun serialize(encoder: Encoder, value: Int) =
        encoder.encodeInt(value)
}