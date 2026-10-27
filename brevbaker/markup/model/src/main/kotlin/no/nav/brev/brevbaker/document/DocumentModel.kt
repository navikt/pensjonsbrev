package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.MarkupModelApi
import no.nav.brev.brevbaker.markup.outline.Block
import java.time.LocalDate

@MarkupModelApi
object DocumentModel {

    fun document(
        tittel: String,
        visTittel: Boolean,
        visLogo: Boolean,
        gjelderNavn: String?,
        gjelderPersonidentifikator: Markup.Personidentifikator?,
        annenMottakerNavn: String?,
        saksnummer: DocumentSaksnummer?,
        dokumentDato: LocalDate?,
        blocks: List<Block>,
    ): Document = Document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        gjelderNavn = gjelderNavn,
        gjelderPersonidentifikator = gjelderPersonidentifikator,
        annenMottakerNavn = annenMottakerNavn,
        saksnummer = saksnummer,
        dokumentDato = dokumentDato,
        blocks = blocks,
    )

    fun documentSaksnummer(
        saksnummer: Markup.Saksnummer,
        visFooter: Boolean,
    ): DocumentSaksnummer = DocumentSaksnummer(
        saksnummer = saksnummer,
        visFooter = visFooter,
    )

    fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
        DocumentPDFRequest(document = document, spraak = spraak)
}
