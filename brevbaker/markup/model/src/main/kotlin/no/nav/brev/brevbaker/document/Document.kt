package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.cleanBlocks
import no.nav.brev.brevbaker.markup.outline.Block
import java.time.LocalDate
import java.util.Objects

class Document internal constructor(
    val tittel: String,
    val visTittel: Boolean,
    val visLogo: Boolean,
    val saksinformasjon: DocumentSaksinformasjon?,
    val dokumentDato: LocalDate?,
    val blocks: List<Block>,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Document) return false
        return tittel == other.tittel &&
                visTittel == other.visTittel &&
                visLogo == other.visLogo &&
                saksinformasjon == other.saksinformasjon &&
                dokumentDato == other.dokumentDato &&
                blocks == other.blocks
    }

    override fun hashCode() = Objects.hash(tittel, visTittel, visLogo, saksinformasjon, dokumentDato, blocks)

    override fun toString(): String =
        "Document(tittel=$tittel, visTittel=$visTittel, visLogo=$visLogo, saksinformasjon=$saksinformasjon, dokumentDato=$dokumentDato, blocks=$blocks)"

    fun clean(): Document = Document(
        tittel = this.tittel,
        visTittel = this.visTittel,
        visLogo = this.visLogo,
        saksinformasjon = this.saksinformasjon,
        dokumentDato = this.dokumentDato,
        blocks = this.blocks.cleanBlocks()
    )
}

/**
 * Saksinformasjonen som er knyttet til et [Document].
 *
 * Strukturen er laget slik at valgene ikke kan settes uten dataene de trenger: [visFooter] krever
 * saksnummer, som alltid er med her, og saksinformasjonsblokken øverst i dokumentet vises kun når
 * [mottaker] er satt.
 */
class DocumentSaksinformasjon internal constructor(
    val saksnummer: Markup.Saksnummer,
    val visFooter: Boolean,
    val mottaker: DocumentMottaker?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is DocumentSaksinformasjon) return false
        return saksnummer == other.saksnummer &&
                visFooter == other.visFooter &&
                mottaker == other.mottaker
    }

    override fun hashCode() = Objects.hash(saksnummer, visFooter, mottaker)

    override fun toString(): String =
        "DocumentSaksinformasjon(saksnummer=$saksnummer, visFooter=$visFooter, mottaker=$mottaker)"
}

/**
 * Mottakeropplysningene som vises i saksinformasjonsblokken øverst i et [Document].
 *
 * Tilsvarer `Saksinformasjon` for brev, men uten saksnummer og dokumentdato: saksnummeret ligger i
 * [DocumentSaksinformasjon], og datoen er et eget element som kan vises uavhengig av
 * saksinformasjonen.
 */
class DocumentMottaker internal constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is DocumentMottaker) return false
        return gjelderNavn == other.gjelderNavn &&
                gjelderPersonidentifikator == other.gjelderPersonidentifikator &&
                annenMottakerNavn == other.annenMottakerNavn
    }

    override fun hashCode() = Objects.hash(gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn)

    override fun toString(): String =
        "DocumentMottaker(gjelderNavn=$gjelderNavn, gjelderPersonidentifikator=$gjelderPersonidentifikator, annenMottakerNavn=$annenMottakerNavn)"
}