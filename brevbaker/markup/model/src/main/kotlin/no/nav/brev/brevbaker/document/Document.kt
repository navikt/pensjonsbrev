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
    val saksinformasjon: DocumentSaksinformasjon?,
    val dokumentDato: LocalDate?,
    val blocks: List<Block>,
)

/**
 * Saksinformasjonen som er knyttet til et [Document].
 *
 * Strukturen er laget slik at valgene ikke kan settes uten dataene de trenger: [visFooter] krever
 * saksnummer, som alltid er med her, og saksinformasjonsblokken øverst i dokumentet vises kun når
 * [mottaker] er satt.
 */
@ConsistentCopyVisibility
data class DocumentSaksinformasjon internal constructor(
    val saksnummer: Markup.Saksnummer,
    val visFooter: Boolean,
    val mottaker: DocumentMottaker?,
)

/**
 * Mottakeropplysningene som vises i saksinformasjonsblokken øverst i et [Document].
 *
 * Tilsvarer `Saksinformasjon` for brev, men uten saksnummer og dokumentdato: saksnummeret ligger i
 * [DocumentSaksinformasjon], og datoen er et eget element som kan vises uavhengig av
 * saksinformasjonen.
 */
@ConsistentCopyVisibility
data class DocumentMottaker internal constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
)

fun Document.clean(): Document = copy(blocks = blocks.cleanBlocks())