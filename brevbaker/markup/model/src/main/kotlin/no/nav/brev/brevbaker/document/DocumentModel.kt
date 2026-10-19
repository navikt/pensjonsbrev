@file:Suppress("DEPRECATION")

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
        saksinformasjon = null,
        dokumentDato = dokumentDato,
        blocks = blocks,
    )

    @Deprecated("Bruk document med gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn og saksnummer")
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
        gjelderNavn = null,
        gjelderPersonidentifikator = null,
        annenMottakerNavn = null,
        saksnummer = null,
        saksinformasjon = saksinformasjon,
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

    @Deprecated("Bruk documentSaksnummer og feltene direkte på document")
    fun documentSaksinformasjon(
        saksnummer: Markup.Saksnummer,
        visFooter: Boolean,
        mottaker: DocumentMottaker?,
    ): DocumentSaksinformasjon = DocumentSaksinformasjon(
        saksnummer = saksnummer,
        visFooter = visFooter,
        mottaker = mottaker,
    )

    @Deprecated("Bruk gjelderNavn, gjelderPersonidentifikator og annenMottakerNavn direkte på document")
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
