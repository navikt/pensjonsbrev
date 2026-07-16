package no.nav.brev.brevbaker.markup

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate

@ConsistentCopyVisibility
@Serializable
data class LetterMarkup internal constructor(
    val title1: List<Text>,
    val saksinformasjon: Saksinformasjon,
    val blocks: List<Block>,
    val signatur: Signatur,
    val version: Int = VERSION,
) {
    companion object {
        const val VERSION = 2
    }
}

@ConsistentCopyVisibility
@Serializable
data class Attachment internal constructor(
    val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
)

@ConsistentCopyVisibility
@Serializable
data class Saksinformasjon internal constructor(
    val gjelderNavn: String,
    val gjelderFoedselsnummer: Foedselsnummer,
    val annenMottakerNavn: String?,
    val saksnummer: Saksnummer,
    @Serializable(with = LocalDateSerializer::class)
    val dokumentDato: LocalDate,
)

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

@ConsistentCopyVisibility
@Serializable
data class Signatur internal constructor(
    val hilsenTekst: String,
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
)

@ConsistentCopyVisibility
@Serializable
data class SaksbehandlerSignatur internal constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
)

@ConsistentCopyVisibility
@Serializable
data class PDFTittel internal constructor(
    val title1: List<Text>,
)
