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
    val gjelderNavn: String?,
    val gjelderPersonidentifikator: Markup.Personidentifikator?,
    val annenMottakerNavn: String?,
    val saksnummer: DocumentSaksnummer?,
    val dokumentDato: LocalDate?,
    val blocks: List<Block>,
)

@ConsistentCopyVisibility
data class DocumentSaksnummer internal constructor(
    val saksnummer: Markup.Saksnummer,
    val visFooter: Boolean,
)

fun Document.clean(): Document = copy(blocks = blocks.cleanBlocks())
