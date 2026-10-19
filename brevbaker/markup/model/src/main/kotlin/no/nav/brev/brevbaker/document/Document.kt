package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.cleanBlocks
import no.nav.brev.brevbaker.markup.outline.Block
import java.time.LocalDate

@ConsistentCopyVisibility
data class Document internal constructor(
    val tittel: String,
    val visTittel: Boolean,
    val visLogo: Boolean,
    @Deprecated("Bruk gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn og saksnummer")
    val saksinformasjon: DocumentSaksinformasjon?,
    val dokumentDato: LocalDate?,
    val blocks: List<Block>,
    val gjelderNavn: String?,
    val gjelderPersonidentifikator: Markup.Personidentifikator?,
    val annenMottakerNavn: String?,
    val saksnummer: DocumentSaksnummer?,
) {
    init {
        @Suppress("DEPRECATION")
        require(saksinformasjon == null || listOf(gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn, saksnummer).all { it == null }) {
            "saksinformasjon kan ikke kombineres med gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn eller saksnummer"
        }
    }
}

@ConsistentCopyVisibility
data class DocumentSaksnummer internal constructor(
    val saksnummer: Markup.Saksnummer,
    val visFooter: Boolean,
)

@Deprecated("Bruk DocumentSaksnummer og feltene direkte på Document")
@ConsistentCopyVisibility
data class DocumentSaksinformasjon internal constructor(
    val saksnummer: Markup.Saksnummer,
    val visFooter: Boolean,
    @Suppress("DEPRECATION")
    val mottaker: DocumentMottaker?,
)

@Deprecated("Bruk gjelderNavn, gjelderPersonidentifikator og annenMottakerNavn direkte på Document")
@ConsistentCopyVisibility
data class DocumentMottaker internal constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
)

fun Document.clean(): Document = copy(blocks = blocks.cleanBlocks())
