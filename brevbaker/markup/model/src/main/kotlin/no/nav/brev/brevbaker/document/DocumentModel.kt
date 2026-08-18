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
        blocks: List<Block>,
    ): Document = Document(
        tittel = tittel,
        visTittel = visTittel,
        visLogo = visLogo,
        saksinformasjon = saksinformasjon,
        dokumentDato = dokumentDato,
        blocks = blocks,
    )

    fun documentSaksinformasjon(
        saksnummer: Markup.Saksnummer,
        visFooter: Boolean,
        mottaker: DocumentMottaker?,
    ): DocumentSaksinformasjon = DocumentSaksinformasjon(
        saksnummer = saksnummer,
        visFooter = visFooter,
        mottaker = mottaker,
    )

    fun documentMottaker(
        gjelderNavn: String,
        gjelderPersonidentifikator: Markup.Personidentifikator,
        annenMottakerNavn: String?,
    ): DocumentMottaker = DocumentMottaker(
        gjelderNavn = gjelderNavn,
        gjelderPersonidentifikator = gjelderPersonidentifikator,
        annenMottakerNavn = annenMottakerNavn,
    )

    fun documentPDFRequest(document: Document, spraak: Markup.Spraak): DocumentPDFRequest =
        DocumentPDFRequest(document = document, spraak = spraak)
}
