package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.outline.Block
import java.time.LocalDate

@ConsistentCopyVisibility
data class Document internal constructor(
    val tittel: String,
    val visTittel: Boolean,
    val visLogo: Boolean,
    val saksinformasjon: DocumentSaksinformasjon?,
    val dokumentDato: LocalDate?,
    val visFooter: Boolean,
    val blocks: List<Block>,
    val version: Int = VERSION,
) {
    init {
        require(!visFooter || saksinformasjon != null) {
            "visFooter krever saksinformasjon: footeren viser saksnummer"
        }
    }

    companion object {
        const val VERSION = 1
    }
}

/**
 * Saksinformasjonen som vises øverst i et [Document].
 *
 * Tilsvarer `Saksinformasjon` for brev, men uten dokumentdato: i et dokument er datoen et eget
 * element som kan vises uavhengig av saksinformasjonen.
 */
@ConsistentCopyVisibility
data class DocumentSaksinformasjon internal constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
    val saksnummer: Markup.Saksnummer,
)
