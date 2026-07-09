package no.nav.brev.brevbaker.markup

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

enum class ElementTags {
    FRITEKST,
    REDIGERBAR_DATA,
}

enum class Brevtype {
    VEDTAKSBREV,
    INFORMASJONSBREV,
}

@Serializable
@JvmInline
value class Foedselsnummer internal constructor(val value: String) {
    override fun toString() = value
}

@Serializable
@JvmInline
value class Saksnummer internal constructor(val saksnummer: String) {
    override fun toString() = saksnummer
}

internal object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.time.LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate =
        LocalDate.parse(decoder.decodeString())
}
