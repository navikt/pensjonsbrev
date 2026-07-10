package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable
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
    override val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
) : AttachmentTitle

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

/** Felles tittel-kontrakt for elementer med egen tittel ([Attachment], [PDFTittel]). */
interface AttachmentTitle {
    val title1: List<Text>
}

/** En frittstående PDF-tittel. */
@ConsistentCopyVisibility
@Serializable
data class PDFTittel internal constructor(
    override val title1: List<Text>,
) : AttachmentTitle

/**
 * Et [LetterMarkup] beriket med metadata: hvilke datafelter brevet bruker ([letterDataUsage]) og
 * hvilken [Brevtype] det er. Beregnet på interne konsumenter (brevbaker/skribenten).
 */
@ConsistentCopyVisibility
@Serializable
data class LetterMarkupWithDataUsage internal constructor(
    val markup: LetterMarkup,
    val letterDataUsage: Set<Property>,
    val brevtype: Brevtype,
) {
    /** Et enkelt datafelt (type og property) brevet leser fra. */
    @ConsistentCopyVisibility
    @Serializable
    data class Property internal constructor(
        val typeName: String,
        val propertyName: String,
    )
}
