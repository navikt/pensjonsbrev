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

/**
 * Et ferdig bygget brev i markup-format: tittel, saksinformasjon, brødtekst ([blocks]) og signatur.
 * Bygg via DSL-en ([no.nav.brev.brevbaker.markup.dsl.letterMarkup]) og serialiser med [toJson].
 * [version] angir markup-formatets versjon på wire.
 */
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

/** Et vedlegg til et brev: egen tittel og brødtekst, med valgfri gjentakelse av saksinformasjon. */
@ConsistentCopyVisibility
@Serializable
data class Attachment internal constructor(
    val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
)

/** Informasjon om saken og mottakeren brevet gjelder. */
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

/** Fødselsnummeret til den brevet gjelder. */
@Serializable
@JvmInline
value class Foedselsnummer internal constructor(val value: String) {
    override fun toString() = value
}

/** Saksnummeret brevet er knyttet til. */
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

/** Avsluttende hilsen og avsenderinformasjon for brevet. */
@ConsistentCopyVisibility
@Serializable
data class Signatur internal constructor(
    val hilsenTekst: String,
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
)

/** Navn på saksbehandler(e) som står bak brevet. */
@ConsistentCopyVisibility
@Serializable
data class SaksbehandlerSignatur internal constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
)

/** En frittstående PDF-tittel. */
@ConsistentCopyVisibility
@Serializable
data class PDFTittel internal constructor(
    val title1: List<Text>,
)
