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
        saksinformasjon: DocumentSaksinformasjon?,
        dokumentDato: LocalDate?,
        visFooter: Boolean,
        blocks: List<Block>,
    ): Document = Document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        saksinformasjon = saksinformasjon,
        dokumentDato = dokumentDato,
        visFooter = visFooter,
        blocks = blocks,
    )

    fun documentSaksinformasjon(
        gjelderNavn: String,
        gjelderPersonidentifikator: String,
        annenMottakerNavn: String?,
        saksnummer: String,
    ): DocumentSaksinformasjon = DocumentSaksinformasjon(
        gjelderNavn = gjelderNavn,
        gjelderPersonidentifikator = Markup.Personidentifikator(gjelderPersonidentifikator),
        annenMottakerNavn = annenMottakerNavn,
        saksnummer = Markup.Saksnummer(saksnummer),
    )

    fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
        DocumentPDFRequest(document = document, spraak = spraak)
}
